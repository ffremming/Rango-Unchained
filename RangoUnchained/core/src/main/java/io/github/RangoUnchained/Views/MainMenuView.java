package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.FontUtils;

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
        // Add background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("Background/Background.png")); // Replace with your actual image path
        Image backgroundImage = new Image(backgroundTexture);

        // Make the image fill the screen
        backgroundImage.setFillParent(true);

        // Add to stage first so it's behind everything else
        stage.addActor(backgroundImage);
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RioGrande.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 62; // Set desired font size
        parameter.color = Color.BLACK;
        parameter.spaceX = 2;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Dispose to prevent memory leaks
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label titleLabel = new Label("(Rango Unchained)", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

FontUtils.addFontAndTextButtonStyleToSkin(getSkin(),
    "rioGrandeFont",
    "customLoginStyle",
    "fonts/RioGrande.ttf",
    32,
    Color.WHITE
);
        

        table.add(titleLabel).center().padBottom(30);
        table.row();

        // Display login state
        String userState = game.getIsLoggedIn() ? "Play" : "Play as guest";

        // Add buttons
        if (!game.getIsLoggedIn()){
            ButtonFactory.createButton("Log In", 300, 60, getSkin(), game,  () -> game.setView(new LoginView()), "customLoginStyle", table);
        }

        ButtonFactory.createButton(userState, 300, 60, getSkin(), game,
            () -> game.setView(new SelectLevelView()), "customLoginStyle", table);
        ButtonFactory.createButton("Scoreboard", 300, 60, getSkin(), game,
            () -> game.setView(new ScoreboardView()), "customLoginStyle", table);
        // Add extra options for logged in users
        if (game.getIsLoggedIn()) {
            ButtonFactory.createButton("Multiplayer", 300, 60, getSkin(), game,
                () -> game.setView(new GameLobbyView()), "customLoginStyle", table);

            // Display logged in user
            table.add(new Label("Logged in as: " + game.getCurrentUser().getDisplayName(), labelStyle)).padBottom(20);
            table.row();
            ButtonFactory.createButton("Change username", 300, 60, getSkin(), game,
                () -> game.setView(new CreateUsernameView()), "customLoginStyle", table);
            ButtonFactory.createButton("Log Out", 300, 60, getSkin(), game, () -> {
                game.getFirebaseManager().signOut();
                game.setIsLoggedIn(false);
                game.setView(new MainMenuView());
            }, "customLoginStyle", table);
        }

        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().padBottom(20);

        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, getSkin());
        volumeSlider.setValue(game.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = volumeSlider.getValue();
                game.setVolume(newVolume);
            }
        });
        Label volumeLabel = new Label("Volume: ", labelStyle);
        volumeLabel.setFontScale(1.3f);
        bottomTable.add(volumeLabel);
        bottomTable.add(volumeSlider);

        // Add table to stage
        stage.addActor(table);
        stage.addActor(bottomTable);
    }
}
