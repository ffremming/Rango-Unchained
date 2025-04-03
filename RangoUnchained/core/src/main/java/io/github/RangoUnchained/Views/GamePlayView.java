package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.level.GameLevel;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;

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
        controller.update(delta);
        drawGame();

        if (!pauseMenu.isPaused()) {
            // Update game logic
            controller.step(1 / 60f, 6, 2);
            controller.excecuteRemovelQueue();
            controller.excecuteSpawnQueue();
            controller.checkpoint(delta);

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

        //controller.getPhysicsSystem().getWorld().step(1/60f, 6, 2);
        for (Entity e : controller.getEntities()) {

            BodyComponent comp = (BodyComponent)e.getComponent(BodyComponent.class);

            float angle = comp==null?0:comp.getBody().getAngle();

            Sprite sprite = ((SpriteComponent) e.getComponent(SpriteComponent.class)).getSprite(angle);
            sprite.setRotation(angle * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }

        //box2DDebugRenderer.render(controller.getWorld(), camera.combined);
        updateHeartsUI();
        updateUI();

        batch.end();
        stage.draw();
    }

    private void createUI() {
        TextButton shootButton = ButtonFactory.createButton("Shoot", 300, 60, getSkin(), game,
            () -> controller.handleShoot());
        TextButton pauseButton = ButtonFactory.createButton("Pause", 150, 60, getSkin(), game,
            () -> pauseMenu.togglePause());

        createTable(shootButton).bottom().right().pad(20);
        createTable(pauseButton).top().padTop(50);
        createJoystick();
       createScoreLabel();
       createTimeLabel();
    }

    private void updateUI(){

        Label scoreLabel = stage.getRoot().findActor("scoreLabel");
        if (scoreLabel != null) {
            int newScore = LevelController.getInstance().getScore();
            scoreLabel.setText("Score: " + newScore);
        }

        // Retrieve the time label from the stage
        Label timeLabel = stage.getRoot().findActor("timeLabel");
        if (timeLabel != null) {
            double time = LevelController.getInstance().getTime();
            timeLabel.setText(String.format("%.1f s", time));
        }
    }

    private void updateHeartsUI() {
        // Remove old hearts
        Actor oldHeartContainer = stage.getRoot().findActor("heartContainer");
        if (oldHeartContainer != null) {
            oldHeartContainer.remove();
        }

        // New top-left-aligned heart container
        Table heartTable = new Table();
        heartTable.setName("heartContainer");
        heartTable.top().left().padTop(10).padLeft(10);
        heartTable.setFillParent(true);

        // Load texture (with crisp pixel look)
        Texture heartTexture = new Texture(Gdx.files.internal("UI/pixel_heart.png"));
        heartTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        float heartSize = 24f;
        int playerHealth = LevelController.getInstance().getPlayerHealth();

        for (int i = 0; i < playerHealth; i++) {
            Image heart = new Image(heartTexture);
            heart.setSize(heartSize, heartSize);

            // Add heart with fixed size, no expand/fill
            heartTable.add(heart).size(heartSize, heartSize).padRight(5);
        }

        stage.addActor(heartTable);
    }


    private void createScoreLabel() {
        Skin skin = getSkin();

        // Create a label to display the score
        Label scoreLabel = new Label("Score: 0", skin);
        scoreLabel.setName("scoreLabel"); // Set a name to easily update it later

        scoreLabel.setBounds(5, stage.getHeight()-(44 + 10 + 50/2), 100, 50);

        Table scoreTable = new Table();
        scoreTable.setFillParent(true); // Let the table span the whole stage
        scoreTable.top().right().padTop(10).padLeft(30); // Align top-right with some padding
    
        scoreTable.add(scoreLabel);
        stage.addActor(scoreLabel);
    }

    private void createTimeLabel() {
       
        Skin skin = getSkin();
    
        // Create the label and name it for future updates
        Label timeLabel = new Label("0 s", skin);
        timeLabel.setName("timeLabel");
    
        // Create a table to position the label at the top-right
        Table timeTable = new Table();
        timeTable.setFillParent(true); // Let the table span the whole stage
        timeTable.top().right().padTop(10).padRight(30); // Align top-right with some padding
    
        timeTable.add(timeLabel);
    
        // Add the table to the stage
        stage.addActor(timeTable);
    
    }

    private Table createTable(Button button) {
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
