package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.PlayerInLobby;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class GameLobbyWaitingView extends BaseScreen {
    private final MultiplayerManager dbManager;
    private final LobbyInfo lobby;
    private final Table playerTable;
    private final String currentUid;

    private Label playerCountLabel;
    private Label statusLabel;
    private TextButton startGameButton;
    private TextButton isReadyButton;
    private int level = 1;

    public GameLobbyWaitingView(LobbyInfo lobby) {
        super(GameController.getInstance());
        this.dbManager = GameController.getInstance().getMultiplayerManager();
        this.currentUid = GameController.getInstance().getCurrentUser().uid;
        this.lobby = lobby;
        this.playerTable = new Table();
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

        isReadyButton = ButtonFactory.createButton("Not ready", 300, 60, getSkin(), game,
            this::toggleReadyStatus);

        startGameButton = ButtonFactory.createButton("Start Game", 300, 60, getSkin(), game,
            this::startGame);
        startGameButton.setVisible(false); // Hidden by default
        table.add(isReadyButton).padBottom(10).row();

        table.add(startGameButton).padBottom(10).row();

        TextButton leaveButton = ButtonFactory.createButton("Leave Lobby", 300, 60, getSkin(), game,
            () -> dbManager.leaveLobby(lobby.lobbyId, GameController.getInstance().getCurrentUser(), new MultiplayerManager.Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Gdx.app.postRunnable(() -> game.setView(new GameLobbyView()));
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("Error leaving lobby: " + e.getMessage());
                }
            }));
        table.add(leaveButton).padBottom(10).row();

        stage.addActor(table);

        lobbyListener();
    }

    private void toggleReadyStatus() {
        dbManager.toggleReadyStatus(lobby.lobbyId, currentUid, new MultiplayerManager.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Gdx.app.postRunnable(() -> {
                    isReadyButton.setText(isReadyButton.getText().toString().equals("Ready") ? "Not Ready" : "Ready");
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

                    if (lobby.status.equals("playing")) {
                        startGameLocally(level);
                    }

                    int currentCount = lobby.players != null ? lobby.players.size() : 0;
                    playerCountLabel.setText("Players in Lobby: " + currentCount + " / " + lobby.maxPlayers);

                    boolean isHost = currentUid.equals(lobby.hostUid);

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

                    // Show "Start Game" if current user is host and lobby is full
                    startGameButton.setVisible(isHost && lobby.isAllPlayersReady());
                });
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Error listening to lobby: " + e.getMessage());
            }
        });
    }

    private void startGameLocally(int level) {
        GamePlayView view = new GamePlayView(level);
        view.setMultiplayer(true);
        game.setView(view);
    }

    private void startGame() {
        dbManager.startGame(lobby.lobbyId, new MultiplayerManager.Callback<Void>() {
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
