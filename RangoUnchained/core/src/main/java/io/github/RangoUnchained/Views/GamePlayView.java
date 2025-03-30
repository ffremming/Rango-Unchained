package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class GamePlayView extends BaseScreen {

    private Touchpad touchpad;
    private LevelController controller;
    private Box2DDebugRenderer box2DDebugRenderer;
    private PauseMenu pauseMenu;

    public GamePlayView(int levelNumber) {
        super(GameController.getInstance());
        box2DDebugRenderer = new Box2DDebugRenderer();
        controller = LevelController.getInstance();
        controller.initializeSystems(levelNumber);
        pauseMenu = new PauseMenu(game, levelNumber);
    }

    @Override
    public void show() {
        super.show();

        createUI();
        controller.getSystem(InputSystem.class).setTouchpad(touchpad);
    }

    @Override
    public void render(float delta) {
        // Clear the screen and update camera
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        viewport.apply();
        drawGame();

        if (!pauseMenu.isPaused()) {
            // Update game logic
            controller.step(1 / 60f, 6, 2);
            controller.excecuteRemovelQueue();
            controller.excecuteSpawnQueue();

            // Update and draw the UI stage on top of the game
            stage.act(delta);
            stage.draw();
        } else {
            // Update and draw the pause menu on top of everything else
            pauseMenu.act(delta);
            pauseMenu.draw();
        }
    }


    private void drawGame() {
        batch.begin();
        controller.update();
        for (Entity e : controller.getEntities()) {
            Sprite sprite = ((SpriteComponent) e.getComponent(SpriteComponent.class)).getSprite();
            batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        }
        batch.end();
    }

    private void createUI() {
        TextButton shootButton = ButtonFactory.createButton("Shoot", 300, 60, getSkin(), game,
            () -> controller.handleShoot());
        TextButton pauseButton = ButtonFactory.createButton("Pause", 150, 60, getSkin(), game,
            () -> pauseMenu.togglePause());

        createTable(shootButton).bottom().right().pad(20);
        createTable(pauseButton).top().padTop(50);
        createJoystick();
    }

    private Table createTable(Button button){
        Table table = new Table();
        table.setFillParent(true);
        table.add(button);

        stage.addActor(table);
        return table;
    }

    private void createJoystick() {
        Skin skin = getSkin();
        Texture joystickTexture = new Texture("joyStick.png");
        Image joystickImage = new Image(joystickTexture);

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = joystickImage.getDrawable();
        touchpadStyle.knob = new Image(skin.getDrawable("default-round")).getDrawable();

        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(50, 50, 100, 100);
        stage.addActor(touchpad);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        LevelController.resetInstance();
        controller = null;
        pauseMenu = null;
    }

}
