package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.level.GameLevel;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;

public class PauseMenu extends Stage {
    private boolean isPaused = false;
    private final GameController game;
    private final int levelNumber;

    public PauseMenu(GameController game, int levelNumber) {
        super(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        this.game = game;
        this.levelNumber = levelNumber;
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(ButtonFactory.createButton("Back to game", 300, 60, GameController.getSkin(), game, this::togglePause)).center().padBottom(20);
        table.row();
        table.add(ButtonFactory.createButton("Restart", 300, 60, GameController.getSkin(), game, this::restart)).center().padBottom(20);
        table.row();
        table.add(ButtonFactory.createButton("End game", 300, 60, GameController.getSkin(), game, this::endGame)).center().padBottom(20);

        table.row();

        addActor(table);
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (!isPaused) {
            if (game.getScreen() instanceof BaseScreen) {
                Gdx.input.setInputProcessor(((BaseScreen) game.getScreen()).getStage());
            }
        } else {
            Gdx.input.setInputProcessor(this);
        }
    }

    private void restart() {
        isPaused = false;
        LevelController.resetInstance();
        Gdx.input.setInputProcessor(null);
        game.setView(new GamePlayView(levelNumber));
        GameLevel.resetCheckpoint();
    }

    private void endGame() {
        game.setView(new GameOverView());
        GameLevel.resetCheckpoint();
    }

    public boolean isPaused() {
        return isPaused;
    }
}
