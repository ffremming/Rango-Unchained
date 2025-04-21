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
        // table.setFillParent(true);
        table.top().padTop(20);
        table.defaults().padLeft(20).center();

        // table.add().expandX(); // helps make the table take full width


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
        
        LabelFactory.createLabel("(Rango Unchained)", getSkin(), "titleFont", Color.BLACK, 1000, BUTTON_WIDTH, 30, table).center();

        // Display login state
        String userState = game.getIsLoggedIn() ? "Play" : "Play as guest";

        // Add buttons
        if (!game.getIsLoggedIn()){
            ButtonFactory.createDefaultButton("Log In", () -> game.setView(new LoginView()), table);
        }

        ButtonFactory.createDefaultButton(userState, () -> game.setView(new SelectLevelView()), table);
        ButtonFactory.createDefaultButton("Scoreboard", () -> game.setView(new ScoreboardView()), table);
        // Add extra options for logged in users
        if (game.getIsLoggedIn()) {
            ButtonFactory.createDefaultButton("Multiplayer", () -> game.setView(new GameLobbyView()), table);

            // Display logged in user
            LabelFactory.createLabel("Logged in as: " + game.getCurrentUser().getDisplayName(), getSkin(), "rioGrandeFont", Color.BLACK, 1000, BUTTON_WIDTH, 20, table).row();

            ButtonFactory.createDefaultButton("Change username", () -> game.setView(new CreateUsernameView()), table);
            ButtonFactory.createDefaultButton("Log Out", () -> {
                game.getFirebaseManager().signOut();
                game.setIsLoggedIn(false);
                game.setView(new MainMenuView());
            }, table);
        }

        Table rightTable = new Table();
        rightTable.setFillParent(true);
        rightTable.bottom().padBottom(20);
        
        Table volumeSlider = SliderFactory.createVolumeSlider(game, getSkin()); 
        table.add(volumeSlider).width(BUTTON_WIDTH).row();;

        // Add table to stage
        // stage.addActor(table);
        // stage.addActor(rightTable);


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
    }
}