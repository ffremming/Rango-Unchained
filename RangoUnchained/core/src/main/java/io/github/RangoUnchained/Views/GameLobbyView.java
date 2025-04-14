package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import java.util.List;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class GameLobbyView extends BaseScreen {
    private final MultiplayerManager dbManager;
    private final UserInfo currentUser;

    public GameLobbyView() {
        super(GameController.getInstance());
        this.dbManager = GameController.getInstance().getMultiplayerManager();
        this.currentUser = GameController.getInstance().getCurrentUser();
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        Label titleLabel = new Label("Create a new Lobby", getSkin());
        Label publicLabel = new Label("Public Lobbies", getSkin());


        // Create left table for lobby creation and joining
        Table leftTable = new Table();
        leftTable.top().padRight(50);

        leftTable.add(titleLabel).center().padBottom(20).row();

        // Input for maxPlayers
        Label maxLabel = new Label("Max Players:", getSkin());
        TextField maxPlayersField = new TextField("4", getSkin());
        leftTable.add(maxLabel).padBottom(5).row();
        leftTable.add(maxPlayersField).width(100).padBottom(20).row();

        // Public/Private checkbox
        CheckBox publicCheckBox = new CheckBox(" Public Lobby", getSkin());
        publicCheckBox.setChecked(true);
        leftTable.add(publicCheckBox).padBottom(20).row();

        // Create lobby button
        ButtonFactory.createButton("Create Lobby", 300, 60, getSkin(), game, () -> {
            int maxPlayers;
            try {
                maxPlayers = Integer.parseInt(maxPlayersField.getText().trim());
            } catch (NumberFormatException e) {
                // If the input is not a valid number, default to 4
                maxPlayers = 4;
            }
            boolean isPublic = publicCheckBox.isChecked();
            createLobby(currentUser, isPublic, maxPlayers);
        }, "customLoginStyle", leftTable);

        // Manual join by code
        leftTable.add(new Label("Or join by code:", getSkin())).center().padBottom(10).row();
        Label joinLabel = new Label("Enter Lobby Code:", getSkin());
        TextField codeField = new TextField("", getSkin());
        leftTable.add(joinLabel).padBottom(5).row();
        leftTable.add(codeField).width(200).padBottom(10).row();
        ButtonFactory.createButton("Join Lobby", 300, 60, getSkin(), game,
            () -> joinLobby(codeField.getText()), "customLoginStyle", leftTable);

        // Create right table for displaying public lobbies
        Table rightTable = new Table();
        rightTable.top().padLeft(50);
        rightTable.add(publicLabel).padBottom(20).row();

        // Fetch and display public lobbies
        displayLobbies(rightTable);

        // Add both tables to the root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.add(ButtonFactory.createButton("Back", getSkin(), game,
            this::backToMenu, "customLoginStyle")).left().padTop(20).padBottom(15);
        rootTable.add(new Label("Multiplayer", getSkin())).left().padBottom(10);
        rootTable.row();

        rootTable.add(leftTable).top();
        rootTable.add(rightTable).top();

        stage.addActor(rootTable);
    }

    private void displayLobbies(Table table) {
        dbManager.fetchPublicLobbies(new MultiplayerManager.Callback<List<LobbyInfo>>() {
            @Override
            public void onSuccess(List<LobbyInfo> lobbies) {
                Gdx.app.postRunnable(() -> {
                    table.clear();

                    if (lobbies.isEmpty()) {
                        table.add(new Label("No public lobbies available.", getSkin())).padBottom(20).row();
                        return;
                    }

                    for (LobbyInfo lobby : lobbies) {
                        String text = "Join Lobby " + lobby.lobbyId + " (" + lobby.players.size() + "/" + lobby.maxPlayers + ")";
                        ButtonFactory.createButton(text, 300, 50, getSkin(), game,
                            () -> joinLobby(lobby.lobbyId), "customLoginStyle", table);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Gdx.app.postRunnable(() -> {
                    table.clear();
                    table.add(new Label("Error loading lobbies: " + e.getMessage(), getSkin())).padBottom(10).row();
                });
            }
        });
    }

    private void backToMenu() {
        game.setView(new MainMenuView());
        dbManager.removePublicLobbiesListener();
    }

    private void createLobby(UserInfo user, boolean isPublic, int maxPlayers) {
        dbManager.createLobby(user, isPublic, maxPlayers, new MultiplayerManager.Callback<LobbyInfo>() {
            @Override
            public void onSuccess(LobbyInfo lobby) {
                Gdx.app.postRunnable(() -> game.setView(new GameLobbyWaitingView(lobby)));
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Error creating lobby: " + e.getMessage());
            }
        });
    }

    private void joinLobby(String lobbyId) {
        dbManager.joinLobby(lobbyId, currentUser, new MultiplayerManager.Callback<LobbyInfo>() {
            @Override
            public void onSuccess(LobbyInfo lobby) {
                Gdx.app.postRunnable(() -> game.setView(new GameLobbyWaitingView(lobby)));
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Error joining lobby: " + e.getMessage());
            }
        });
    }

    @Override
    public void dispose() {
        dbManager.removePublicLobbiesListener();
        super.dispose();
    }

}
