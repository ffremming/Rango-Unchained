package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class GamePlayView extends BaseScreen {
    private int level;
    private Texture playerTexture;
    private float playerX, playerY;

//    public GamePlayView(int level) {
//        super(GameController.getInstance());
//        this.level = level;
//    }
    public GamePlayView() {
        super(GameController.getInstance());
    }

    @Override
    public void show() {
        super.show();
        playerTexture = new Texture("Rango/Rango.png");
        playerX = Gdx.graphics.getWidth() / 2f;
        playerY = Gdx.graphics.getHeight() / 2f;

        createUI();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(playerTexture, playerX, playerY, 64, 64);
        batch.end();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        // Create Game Over button
        TextButton gameOverButton = ButtonFactory.createButton("End Game", 300, 60, getSkin(), game, () -> game.setView(new GameOverView()));

        table.add(gameOverButton).center();

        stage.addActor(table);
    }

    @Override
    public void hide() {
        super.hide();
        playerTexture.dispose();
    }

}
