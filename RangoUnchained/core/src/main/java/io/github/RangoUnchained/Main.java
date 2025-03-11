package io.github.RangoUnchained;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.RangoUnchained.Views.MainMenuView;
import io.github.RangoUnchained.Views.ScoreboardView;

public class Main extends Game {
    private static SpriteBatch batch;
    private static BitmapFont font;
    private static Skin skin;
    private Screen currentScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        skin = new Skin(com.badlogic.gdx.Gdx.files.internal("skin/uiskin.json")); // Load skin ONCE

        changeScreen(new MainMenuView(this));
    }

    public static SpriteBatch getBatch() {
        if (batch == null) batch = new SpriteBatch();
        return batch;
    }

    public static BitmapFont getFont() {
        if (font == null) font = new BitmapFont();
        return font;
    }

    public static Skin getSkin() {
        return skin;
    }

    public void changeScreen(Screen newScreen) {
        if (currentScreen != null && currentScreen != newScreen) {
            currentScreen.hide();

            // Dispose only if the screen is not meant to be reused
            if (!(currentScreen instanceof ScoreboardView)) {
                currentScreen.dispose();
            }
        }

        currentScreen = newScreen;
        setScreen(currentScreen);
    }


    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        skin.dispose();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
