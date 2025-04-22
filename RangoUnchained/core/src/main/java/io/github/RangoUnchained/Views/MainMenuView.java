package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.FontUtils;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.ScrollUtil;
import io.github.RangoUnchained.Views.Utils.SliderFactory;

public class MainMenuView extends BaseScreen {

    public MainMenuView() {
        super(GameController.getInstance());
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        
        Table table = new Table();
        table.top().padTop(20);
        table.defaults().center();
        table.add().expandX(); // helps make the table take full width
        table.center().row();

        FontUtils.addFontAndTextButtonStyleToSkin(getSkin(),
            "rioGrandeFont",
            "customLoginStyle",
            "fonts/RioGrande.ttf",
            32,
            Color.WHITE
        );
        FontUtils.addFontAndTextButtonStyleToSkin(getSkin(),
            "titleFont",
            "titleStyle",
            "fonts/RioGrande.ttf",
            50,
            Color.WHITE
        );
        FontUtils.addFontAndTextButtonStyleToSkin(getSkin(),
            "defaultFont",
            "defaultStyle",
            "fonts/Default.ttf",
            32,
            Color.BLACK
        );
        FontUtils.addFontAndTextButtonStyleToSkin(getSkin(),
            "textFieldFont",
            "textFieldStyle",
            "fonts/Default.ttf",
            30,
            Color.WHITE
        );
        FontUtils.addFontAndTextButtonStyleToSkin(getSkin(),
            "errorFont",
            "errorStyle",
            "fonts/Default.ttf",
            25,
            Color.RED
        );
    if (game.getIsLoggedIn())
        LabelFactory.createLabel("Hi, " + game.getCurrentUser().getDisplayName(), getSkin(), "defaultFont", Color.BLACK, 10, table).row();

        // Display login state
        String userState = game.getIsLoggedIn() ? "Play" : "Play as guest";

        Table volumeTable = new Table();
        Table volumeSlider = SliderFactory.createVolumeSlider(game, getSkin()); 
        volumeTable.add(volumeSlider).width(BUTTON_WIDTH).pad(20);
        Table sfxSlider = SliderFactory.createSFXVolumeSlider(game, GameController.getSkin());
        volumeTable.add(sfxSlider).width(BUTTON_WIDTH).pad(20);
        // Add buttons
        if (!game.getIsLoggedIn()){
            ButtonFactory.createDefaultButton("Log In", () -> game.setView(new LoginView()), table);
        }

        ButtonFactory.createDefaultButton(userState, () -> game.setView(new SelectLevelView()), table);
        ButtonFactory.createDefaultButton("Scoreboard", () -> game.setView(new ScoreboardView()), table);
        // Add extra options for logged in users
        if (game.getIsLoggedIn()) {
            ButtonFactory.createDefaultButton("Multiplayer", () -> game.setView(new GameLobbyView()), table);

            ButtonFactory.createDefaultButton("Change username", () -> game.setView(new CreateUsernameView()), table);
            
            volumeTable.add(ButtonFactory.createButton("Log Out", getSkin(), game, () -> {
                game.getFirebaseManager().signOut();
                game.setIsLoggedIn(false);
                game.setView(new MainMenuView());
            }, "customLoginStyle")).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
        }



        ScrollPane scrollPane = ScrollUtil.createStyledScrollPane(table);
        if (game.getIsLoggedIn()) {
            scrollPane.setFadeScrollBars(false);
        } else {
            scrollPane.setFadeScrollBars(true);
        }
        Gdx.app.postRunnable(() -> {
            scrollPane.layout();          
            scrollPane.setScrollY(0);      
            scrollPane.updateVisualScroll();
        });

        // Main table that fills the screen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        LabelFactory.createLabel("(Rango Unchained)", getSkin(), "titleFont", Color.BLACK, TITLE_PADDING, mainTable);
        mainTable.top().add(scrollPane).expandY().width(WORLD_WIDTH - 400).fill().row();
        mainTable.add(volumeTable);
        stage.addActor(mainTable);
    }
}

