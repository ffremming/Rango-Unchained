package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
    private Box2DDebugRenderer box2DDebugRenderer;
    private Box2DDebugRenderer debugRenderer;

//    public GamePlayView(int level) {
//        super(GameController.getInstance());
//        this.level = level;
//    }
    public GamePlayView() {
        super(GameController.getInstance());
        box2DDebugRenderer = new Box2DDebugRenderer();
        controller = LevelController.getInstance();
        controller.initializeSystems();
        controller.initializeWorld();
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        super.show();

        createUI();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Update physics world first
        controller.getWorld().step(1/60f, 6, 2);
        controller.handleSpawnRequests();
        controller.removeEntities();

        // Handle input and movement after physics update
        controller.getInputSystem().handleInputSingleplayer();
        controller.getAnimationSystem().designAnimation();
        controller.getMovementSystem().updateEntityPosition();
        controller.getLifeTimeSystem().update(controller.getEntities(),controller);

        // Update sprite positions based on physics bodies
        controller.getPhysicsSystem().updatePhysics();

        // Render everything
        batch.begin();
        for (Entity e : controller.getEntities()) {
            Sprite sprite = ((SpriteComponent) e.getComponent(SpriteComponent.class)).getSprite();
            batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        }

        box2DDebugRenderer.render(controller.getWorld(), camera.combined);
        debugRenderer.render(controller.getWorld(), camera.combined);

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
