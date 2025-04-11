package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.PlayerInLobby;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;

public class GameLobbyWaitingView extends BaseScreen {
    private final MultiplayerManager dbManager;
    private final LobbyInfo lobby;
    private final Table playerTable;
    private final String currentUid;

    private Label playerCountLabel;
    private Label statusLabel;
    private TextButton startGameButton;
    private TextButton isReadyButton;
    private TextField selectedLevel;
    private boolean alreadyStarted = false;
    boolean isHost;
    private int level;

    public GameLobbyWaitingView(LobbyInfo lobby) {
        super(GameController.getInstance());
        this.dbManager = GameController.getInstance().getMultiplayerManager();
        this.currentUid = GameController.getInstance().getCurrentUser().uid;
        this.lobby = lobby;
        this.playerTable = new Table();
        this.isHost = currentUid.equals(lobby.hostUid);
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    public void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        Label lobbyLabel = new Label("Lobby ID: " + lobby.lobbyId, getSkin());
        table.add(lobbyLabel).padBottom(20).row();

        playerCountLabel = new Label("Players in Lobby: ...", getSkin());
        table.add(playerCountLabel).padBottom(10).row();

        table.add(playerCountLabel).padBottom(10).row();
        table.add(playerTable).padBottom(30).row();

        statusLabel = new Label("", getSkin());
        table.add(statusLabel).padBottom(10).row();

        isReadyButton = ButtonFactory.createButton("Ready up!", 300, 60, getSkin(), game,
            this::toggleReadyStatus);

        startGameButton = ButtonFactory.createButton("Start Game", 300, 60, getSkin(), game,
            this::startGame);
        startGameButton.setVisible(false); // Hidden by default
        table.add(isReadyButton).padBottom(10).row();

        table.add(startGameButton).padBottom(10).row();

        TextButton leaveButton = ButtonFactory.createButton("Leave Lobby", 300, 60, getSkin(), game,
            this::leaveLobby);
        table.add(leaveButton).padBottom(10).row();
        Label levelLabel = new Label("Selected Level:", getSkin());
        selectedLevel = new TextField("1", getSkin());
        table.add(levelLabel).padBottom(5).row();
        table.add(selectedLevel).width(100).padBottom(20).row();

        stage.addActor(table);

        lobbyListener();
        levelListener();
    }

    private void leaveLobby() {
        dbManager.leaveLobby(lobby.lobbyId, GameController.getInstance().getCurrentUser(), new MultiplayerManager.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Gdx.app.postRunnable(() -> game.setView(new GameLobbyView()));
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Error leaving lobby: " + e.getMessage());
            }
        });
    }

    private void toggleReadyStatus() {
        dbManager.toggleReadyStatus(lobby.lobbyId, currentUid, new MultiplayerManager.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Gdx.app.postRunnable(() -> {
                    isReadyButton.setText(isReadyButton.getText().toString().equals("Ready up!") ? "Not Ready" : "Ready up!");
                });
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Error toggling ready status: " + e.getMessage());
            }
        });
    }
    private void lobbyListener() {
        dbManager.listenToLobby(lobby.lobbyId, new MultiplayerManager.Callback<LobbyInfo>() {
            @Override
            public void onSuccess(LobbyInfo lobby) {
                Gdx.app.postRunnable(() -> {
                    playerTable.clear();

                    if (lobby.status.equals("playing") && !alreadyStarted) {
                        startGameLocally(level);
                        alreadyStarted = true;
                    }

                    int currentCount = lobby.players != null ? lobby.players.size() : 0;
                    playerCountLabel.setText("Players in Lobby: " + currentCount + " / " + lobby.maxPlayers);

                    for (PlayerInLobby player : lobby.players.values()) {
                        StringBuilder labelText = new StringBuilder();
                        if (lobby.isHost(player.uid)) labelText.append("* ");
                        labelText.append(player.displayName);
                        if (player.uid.equals(currentUid)) labelText.append(" (You)");

                        Label nameLabel = new Label(labelText.toString(), getSkin());
                        Label readyLabel = new Label(Boolean.TRUE.equals(player.isReady) ? " Ready" : " Not Ready", getSkin());

                        Table row = new Table();
                        row.add(nameLabel).left().padRight(10);
                        row.add(readyLabel).left();
                        playerTable.add(row).padBottom(5).row();
                    }

                    // Status display
                    if (currentCount < lobby.maxPlayers) {
                        statusLabel.setText("Waiting for players...");
                    } else {
                        statusLabel.setText("");
                    }

                    // Update displayed level for all clients
                    level = lobby.level != null ? lobby.level : 1;
                    selectedLevel.setText(String.valueOf(level));
                    selectedLevel.setDisabled(!isHost);

                    // Show "Start Game" if current user is host and lobby is full
                    startGameButton.setVisible(isHost && lobby.isAllPlayersReady());
                    startGameButton.setDisabled(level < 1 || level > Constants.LEVELS_COUNT);
                });
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Error listening to lobby: " + e.getMessage());
            }
        });
    }

    private void levelListener() {
        selectedLevel.setTextFieldListener((textField, c) -> {
            if (isHost) {
                try {
                    int newLevel = Integer.parseInt(textField.getText().trim());
                    if (newLevel < 1) {
                        newLevel = 1;
                        textField.setText("1");
                    } else if (newLevel > Constants.LEVELS_COUNT) {
                        newLevel = Constants.LEVELS_COUNT;
                        textField.setText(String.valueOf(Constants.LEVELS_COUNT));
                    }

                    this.level = newLevel;

                    dbManager.setLobbyLevel(lobby.lobbyId, level, new MultiplayerManager.Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            System.out.println("Level updated to " + level);
                        }

                        @Override
                        public void onError(Exception e) {
                            System.err.println("Failed to update level: " + e.getMessage());
                        }
                    });
                } catch (NumberFormatException e) {
                    System.out.println("Invalid level number: " + textField.getText());
                }
            }
        });
    }


    private void startGameLocally(int level) {
        GamePlayView view = new GamePlayView(level, true, lobby);
        Gdx.app.postRunnable(() -> {
            game.setView(view);
        });
    }

    private void startGame() {
        dbManager.startGame(lobby.lobbyId, level, new MultiplayerManager.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                System.out.println("Game started successfully.");
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Failed to start game: " + e.getMessage());
            }
        });
    }
}
