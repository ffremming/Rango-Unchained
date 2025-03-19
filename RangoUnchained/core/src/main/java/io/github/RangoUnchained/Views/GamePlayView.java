package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class GamePlayView extends BaseScreen {
    private int level;
    private Texture playerTexture;
    private float playerX, playerY;
    private LevelController controller;

//    public GamePlayView(int level) {
//        super(GameController.getInstance());
//        this.level = level;
//    }
    public GamePlayView() {
        super(GameController.getInstance());
        controller = LevelController.getInstance();
        controller.initializeSystems();
        controller.initializeWorld();
    }

    @Override
    public void show() {
        super.show();

        createUI();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        controller.getInputSystem().handleInputSingleplayer();
        controller.getMovementSystem().updateEntityPosition();

        for (Entity e : controller.getEntities()) {
            Sprite sprite = ((SpriteComponent) e.getComponent(SpriteComponent.class)).getSprite();
            Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
            batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        }
        controller.getWorld().step(1/60f, 6, 2);
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
        controller.clearSystems();
    }

}
