package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.RangoUnchained.Controllers.GameController;

public abstract class BaseScreen extends ScreenAdapter {

    protected final GameController game;
    protected final SpriteBatch batch;
    protected final BitmapFont font;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;

    public BaseScreen(GameController game) {
        this.game = game;
        this.batch = GameController.getBatch();
        this.font = GameController.getFont();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        stage = new Stage(viewport);
    }

    public Skin getSkin() {
        return GameController.getSkin();
    }
    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        viewport.apply();
        stage.act(delta);
        stage.draw();
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.clear();
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
