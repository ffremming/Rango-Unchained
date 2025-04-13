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
import io.github.RangoUnchained.Controllers.MusicController;

public abstract class BaseScreen extends ScreenAdapter implements MusicProvider {

    protected final GameController game;
    protected final SpriteBatch batch;
    protected final BitmapFont font;
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera camera;

    protected static final float WORLD_WIDTH = 1200;
    protected static final float WORLD_HEIGHT = 540;
    public BaseScreen(GameController game) {
        this.game = game;
        this.batch = GameController.getBatch();
        this.font = GameController.getFont();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport);

        camera.setToOrtho(false);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();
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
    public MusicController.MusicKey getMusicKey(){
        return MusicController.MusicKey.DEFAULT;
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
    }
}
