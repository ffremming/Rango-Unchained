package io.github.RangoUnchained.Controllers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameController extends Game {
    private SpriteBatch batch;
    private Texture image;
    private static ScreenController screenController;
    private static final GameController gameController = new GameController();

    private GameController() {
    }

    public static GameController getInstance(){
        return gameController;
    }

    @Override
    public void create() {
        screenController = ScreenController.getInstance();
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        screenController.render(Gdx.graphics.getDeltaTime());
        screenController.update(batch);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        screenController.dispose();
    }
}

