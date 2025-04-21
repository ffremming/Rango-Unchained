package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.PlayerInLobby;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.TextFieldFactory;

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
    private Timer.Task lobbyPingTask;

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
        addLobbyListener();
    }

    public void createUI() {
        Table table = new Table();
        table.top().padTop(50);
        table.defaults().padLeft(20).center();

        LabelFactory.createLabel("Lobby ID: " + lobby.lobbyId, getSkin(), "defaultFont", null, BUTTON_WIDTH, BUTTON_HEIGHT, 10, table).row();

        playerCountLabel = LabelFactory.createLabel("Players in Lobby: ...", getSkin(), "defaultFont", null);
        table.add(playerCountLabel).padBottom(10).row();

        // table.add(playerCountLabel).padBottom(10).row();
        table.add(playerTable).padBottom(30).row();

        statusLabel = LabelFactory.createLabel("", getSkin(), "defaultFont", null);
        table.add(statusLabel).padBottom(10).row();

        isReadyButton = ButtonFactory.createButton("Ready up!", getSkin(), game,
            this::toggleReadyStatus, "customLoginStyle");

        startGameButton = ButtonFactory.createButton("Start Game", getSkin(), game,
            this::startGame, "customLoginStyle");
        startGameButton.setVisible(false); // Hidden by default
        table.add(isReadyButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(10).row();

        table.add(startGameButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(10).row();

        TextButton leaveButton = ButtonFactory.createButton("Leave Lobby", getSkin(), game,
            this::leaveLobby, "customLoginStyle");
        Label levelLabel = LabelFactory.createLabel("Selected Level:", getSkin(), "defaultFont", null);
        selectedLevel = new TextField("1", getSkin(), "textFieldStyle-textField");
        table.add(leaveButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(10).row();

        Table selectedLevelContainer = TextFieldFactory.createTextField(            
        getSkin(),
        "",
        "textFieldStyle-textField",
        "textfield",
        true,     // transparent background
        60,       // inner padding
        300,      // width
        90,
        selectedLevel
        );      
        table.add(levelLabel).padBottom(5).row();
        table.add(selectedLevelContainer).row();
        // table.add(selectedLevel).width(100).padBottom(20).row();

        // 🔁 ScrollPane wraps your contentTable
        ScrollPane scrollPane = new ScrollPane(table, getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling, allow vertical

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();           // Force layout pass
            scrollPane.setScrollY(0);      // Set scroll to the top
            scrollPane.updateVisualScroll();
        });
        // 📦 Main table that fills the screen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().add(scrollPane).expand().fill().row();
 

        stage.addActor(mainTable);
        levelListener();
    }

    private void leaveLobby() {
        dbManager.leaveLobby(lobby.lobbyId, GameController.getInstance().getCurrentUser(), new MultiplayerManager.Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Gdx.app.postRunnable(() -> game.setView(new GameLobbyView()));
                dbManager.removeLobbyListener();
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
    private void addLobbyListener() {
        // Ensure any existing task is cancelled
        if (lobbyPingTask != null) {
            lobbyPingTask.cancel();
            lobbyPingTask = null;
        }

        // Periodic task to refresh the lobby
        lobbyPingTask = Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
            dbManager.removeLobbyListener();
            dbManager.listenToLobby(lobby.lobbyId, new MultiplayerManager.Callback<LobbyInfo>() {
                @Override
                public void onSuccess(LobbyInfo lobby) {
                    Gdx.app.postRunnable(() -> {
                        playerTable.clear();

                        if ("running".equals(lobby.status) && !alreadyStarted) {
                            startGameLocally(level);
                        }

                        int currentCount = lobby.players != null ? lobby.players.size() : 0;
                        playerCountLabel.setText("Players in Lobby: " + currentCount + " / " + lobby.maxPlayers);

                        for (PlayerInLobby player : lobby.players.values()) {
                            if (player == null) continue; // Skip null players (happens when a player leaves)
                            StringBuilder labelText = new StringBuilder();
                            if (lobby.isHost(player.uid)) labelText.append("- ");
                            labelText.append(player.displayName);
                            if (currentUid.equals(player.uid)) labelText.append(" (You)");


                            Label nameLabel = LabelFactory.createLabel(labelText.toString(), getSkin(), "defaultFont", null);
                            Label readyLabel =  LabelFactory.createLabel(Boolean.TRUE.equals(player.isReady) ? " Ready" : " Not Ready", getSkin(), "defaultFont", null);
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
                    leaveLobby();
                }
            });
        }
        // First delay, then interval in seconds
    }, 0, 60);
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
            alreadyStarted = true;
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

    @Override
    public void hide() {
        super.hide();
        if (lobbyPingTask != null) {
            lobbyPingTask.cancel();
        }
    }
}
