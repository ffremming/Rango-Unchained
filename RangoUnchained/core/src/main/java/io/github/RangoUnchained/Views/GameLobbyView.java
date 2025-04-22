package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.List;
import java.util.regex.Pattern;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.ScrollUtil;
import io.github.RangoUnchained.Views.Utils.TextFieldFactory;

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
        Label titleLabel = LabelFactory.createLabel("Create a new Lobby", getSkin(), "defaultFont", null);
        Label publicLabel =  LabelFactory.createLabel("Public lobbies", getSkin(), "defaultFont", null);


        // Create a table to align UI-elements
        Table table = new Table();

        table.add(titleLabel).center().padBottom(20).row();

        // Input for maxPlayers
        Label maxLabel =  LabelFactory.createLabel("Max players:", getSkin(), "defaultFont", null);
        TextField maxPlayersField = new TextField("", getSkin(), "textFieldStyle-textField");
        maxPlayersField.setMessageText("4");
        Table maxPlayersFieldContainer = TextFieldFactory.createTextField(
            getSkin(),
            "",
            "textFieldStyle-textField",
            "textfield",
            true,     // transparent background
            60,       // inner padding
            100,      // width
            90,
            maxPlayersField
        );       
        table.add(maxLabel).row();
        table.add(maxPlayersFieldContainer).row();
        // Alternative simpler approach
        CheckBox publicCheckBox = new CheckBox("Public Lobby", getSkin());
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle(publicCheckBox.getStyle());
        style.font = getSkin().getFont("defaultFont"); // Use a font name that exists in your skin
        style.fontColor = Color.BLACK; // Set the font color
        
        // Apply the modified style if needed
        publicCheckBox.setChecked(true);
        publicCheckBox.getImage().setScale(0.2f); // Scale the checkbox image directly
        table.add(publicCheckBox).width(publicCheckBox.getImage().getWidth()).height(publicCheckBox.getImage().getHeight()).padBottom(20).row();
        // Create lobby button
        ButtonFactory.createDefaultButton("Create Lobby", () -> {
            int maxPlayers;
            try {
                maxPlayers = Integer.parseInt(maxPlayersField.getText().trim());
            } catch (NumberFormatException e) {
                // If the input is not a valid number, default to 4
                maxPlayers = 4;
            }
            boolean isPublic = publicCheckBox.isChecked();
            createLobby(currentUser, isPublic, maxPlayers);
        }, table).row();

        Table rightTable = new Table();
        rightTable.top().padLeft(50);

        // Manual join by code
        rightTable.add( LabelFactory.createLabel("Or join by code:", getSkin(), "defaultFont", null)).row(); 
        TextField codeField = new TextField("", getSkin(), "textFieldStyle-textField");
        codeField.setMessageText("Lobby Code");
        Table codeFieldContainer = TextFieldFactory.createTextField(
            getSkin(),
            "",
            "textFieldStyle-textField",
            "textfield",
            true,     // transparent background
            60,       // inner padding
            200,      // width
            90,
            codeField
        );

        rightTable.add(codeFieldContainer).row();
        ButtonFactory.createDefaultButton("Join Lobby", () -> joinLobby(codeField.getText()), rightTable);

        Table lobbiesTable = new Table(); 
        lobbiesTable.add(publicLabel);
        rightTable.add(lobbiesTable).padBottom(20).row();

        // Fetch and display public lobbies
        displayLobbies(lobbiesTable);

        // Add both tables to the root table
        Table rootTable = new Table();
        rootTable.top().padTop(20);
        rootTable.defaults().padLeft(20).center();
        rootTable.center();

        rootTable.add(table).top();
        rootTable.add(rightTable).top();

ScrollPane scrollPane = ScrollUtil.createStyledScrollPane(rootTable);

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();           // Force layout pass
            scrollPane.setScrollY(0);      // Set scroll to the top
            scrollPane.updateVisualScroll(); 
        });        
        // 📦 Main table that fills the screen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(LabelFactory.createLabel("Multiplayer", getSkin(), "titleFont", Color.BLACK)).width(300).height(90).row();;
        mainTable.top().add(scrollPane).expand().fill().center().row();
        mainTable.add(ButtonFactory.createButton("Back", getSkin(), game,
        this::backToMenu, "customLoginStyle")).width(300).height(60).center().padTop(20).padBottom(15);
        stage.addActor(mainTable);
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
                        ButtonFactory.createButton(text, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_PADDING, getSkin(), game,
                            () -> joinLobby(lobby.lobbyId), "textFieldStyle", table);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Gdx.app.postRunnable(() -> {
                    table.clear();
                    table.add(LabelFactory.createLabel("Error loading lobbies: " + e.getMessage(), getSkin(), "defaultFont", null));
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

    private static final Pattern LOBBY_ID_OK =
        Pattern.compile("^[A-Za-z0-9_-]+$");
    private void joinLobby(String lobbyId) {
        if (lobbyId == null || !LOBBY_ID_OK.matcher(lobbyId).matches()) return;

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
