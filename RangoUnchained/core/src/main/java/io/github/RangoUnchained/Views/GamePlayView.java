package io.github.RangoUnchained.Views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Controllers.AudioController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.TutorialSystem;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.HintUtil;
import io.github.RangoUnchained.Views.Utils.LabelFactory;

public class GamePlayView extends BaseScreen {

    private Touchpad touchpad;
    private LevelController controller;
    private PauseMenu pauseMenu;
    private boolean isMultiplayer = false;
    private LobbyInfo lobby;
    private final int levelNumber;
    private boolean hasSentFinishData;
    private boolean shouldInitialize = true;
    private boolean initialized = false;
    private Table leftTable;
    private Table centralTable;
    private Table righTable;
    private Table shootTable;


    public GamePlayView(int levelNumber) {
        super(GameController.getInstance());
        this.levelNumber = levelNumber;
        removeBackground();
    }

    // Constructor for multiplayer
    public GamePlayView(int levelNumber, boolean isMultiplayer, LobbyInfo lobby) {
        super(GameController.getInstance());
        this.levelNumber = levelNumber;
        this.isMultiplayer = isMultiplayer;
        this.lobby = lobby;
        removeBackground();
    }

    @Override
    public void show() {
        super.show();

        // Safely initialize a new level
        if (shouldInitialize) {
            shouldInitialize = false;
            Gdx.app.postRunnable(() -> {
                controller = LevelController.getInstance();
                controller.initializeSystems(levelNumber);
                // Initialize pause menu based on multiplayer flag
                pauseMenu = isMultiplayer ? new PauseMenu(game, levelNumber, lobby) : new PauseMenu(game, levelNumber);
                controller.getSystem(InputSystem.class).setTouchpad(touchpad);
                initialized = true;
            });
        }

        createUI();
    }

    @Override
    public AudioController.MusicKey getMusicKey() {
        return AudioController.MusicKey.GAMEPLAY;
    }


    @Override
    public void render(float delta) {
        // Skip rendering if not initialized yet
        if (!initialized) return;

        try{
        // Clear the screen and update camera
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        viewport.apply();

        drawGame();

        // Run game logic only if not paused or multiplayer
        if (!pauseMenu.isPaused() || isMultiplayer) {
            // Update game logic
            controller.step(delta, 6, 2);
            controller.update(delta);
        }
        if (!pauseMenu.isPaused()) {
            getButtonByName("Pause").setVisible(true);
            stage.act(delta);
            stage.draw();
        }
         else {
            pauseMenu.act(delta);
            pauseMenu.draw();
            getButtonByName("Pause").setVisible(false);
        }

        // Game over
            if (controller.isGameOver()) {
                checkGameOver();
            }
        } catch (NullPointerException e) {
            System.out.println("Rendering error: " + e.getMessage());
        }
    }

    private void drawGame() {
        batch.begin();

        for (Entity e : controller.getEntities()) {

            BodyComponent comp = (BodyComponent)e.getComponent(BodyComponent.class);

            float angle = comp==null?0:comp.getBody().getAngle();

            Sprite sprite = ((SpriteComponent) e.getComponent(SpriteComponent.class)).getSprite(angle);
            sprite.setRotation(angle * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }

        updateUI();

        batch.end();
        stage.draw();
    }

    private void createUI() {
        centralTable = new Table();
        centralTable.setFillParent(true); 
        centralTable.top().padTop(40).toFront();
        shootTable = new Table();
        shootTable.setFillParent(true);
        shootTable.right().bottom().padBottom(30).padRight(50).toFront();

        
        TextButton pauseButton = ButtonFactory.createButton("Pause", getSkin(), game, () -> pauseMenu.togglePause(), "customLoginStyle");
        centralTable.setName("Pause");

        centralTable.add(pauseButton).center().width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(BUTTON_PADDING);
        ButtonFactory.createButton("LICK EM!", BUTTON_WIDTH/2, 50, 0, getSkin(), game, () -> controller.handleShoot(), "customLoginStyle", shootTable);
        createJoystick();
        createScoreLabel();
        createTimeLabel();
        createTutorialLabel();

        stage.addActor(centralTable);
        stage.addActor(shootTable);
    }

    private void updateUI(){
        try {
            Label scoreLabel = stage.getRoot().findActor("scoreLabel");
            if (scoreLabel != null) {
                int newScore = LevelController.getInstance().getScore();
                scoreLabel.setText("Score: " + newScore);
            }

            // Retrieve the time label from the stage
            Label timeLabel = stage.getRoot().findActor("timeLabel");
            if (timeLabel != null) {
                double time = LevelController.getInstance().getLevel().getTimer().getTime();
                timeLabel.setText(String.format("%.1f s", time));
            }
            Label tutorialLabel = stage.getRoot().findActor("tutorialLabel");
            if (tutorialLabel != null) {
                TutorialSystem tutorialSystem = LevelController.getInstance().getSystem(TutorialSystem.class);
                System.out.println("Tutorial text: " + tutorialSystem.getTutorialMessage());
                if (tutorialSystem!= null){
                    tutorialLabel.setText(tutorialSystem.getTutorialMessage());
                }

            }
            updateHeartsUI();
            updatePowerupUI();

        } catch (NullPointerException e) {
            // Handle the case where the label is not found
            System.out.println("Label not found: " + e.getMessage());
        }
    }

    private void updatePowerupUI() {
        ArrayList<Integer> powerupList = LevelController.getInstance().getPlayerActivePowerup();
        // Remove old hearts
        Actor oldPowerups = stage.getRoot().findActor("powerupContainer");
        if (oldPowerups != null) {
            oldPowerups.remove();
        }

        // New top-left-aligned heart container
        Table powerups = new Table();
        powerups.setName("powerupContainer");
        powerups.top().right().padTop(50).padRight(15);
        powerups.setFillParent(true);

        // Load texture (with crisp pixel look)
        Texture heartTexture = new Texture(Gdx.files.internal("UI/Pixel_heart.png"));
        heartTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        float size = 32f;

        for (int powerup : powerupList) {
            Image powerupImage = null;
            if (powerup == PowerUpComponent.SPEED) {
                powerupImage = new Image(new Texture(Gdx.files.internal("Powerup/Speed.png")));
                powerups.add(powerupImage).size(size, size).padRight(5);

                } else if (powerup == PowerUpComponent.SHIELD) {
                powerupImage = new Image(new Texture(Gdx.files.internal("Powerup/Shield.png")));
                powerups.add(powerupImage).size(size, size).padRight(5);
                }
        }
        stage.addActor(powerups);
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
        heartTable.top().left().padTop(20).padLeft(15);
        heartTable.setFillParent(true);

        // Load texture (with crisp pixel look)
        Texture heartTexture = new Texture(Gdx.files.internal("UI/Pixel_heart.png"));
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

    private void createTutorialLabel() {
        Skin skin = getSkin();

        // Create a label to display the score
        Label tutorialLabel =  LabelFactory.createLabel("", skin, "defaultFont", Color.BLACK);
        tutorialLabel.setName("tutorialLabel"); // Set a name to easily update it later

        tutorialLabel.setBounds(stage.getWidth()/2, stage.getHeight()/2, 100, 50);

        centralTable.add(tutorialLabel).expandX();

    }

    private void createScoreLabel() {
        Skin skin = getSkin();

        // Create a label to display the score
        Label scoreLabel = LabelFactory.createLabel("", skin, "defaultFont", null);
        scoreLabel.setName("scoreLabel"); // Set a name to easily update it later

        // scoreLabel.setBounds(10, stage.getHeight()-(44 + 5 + 50/2), 100, 50);

        Table scoreTable = new Table();
        scoreTable.setFillParent(true); // Let the table span the whole stage
        scoreTable.top().left().padTop(50).padLeft(15);

        scoreTable.add(scoreLabel);
        stage.addActor(scoreTable);
    }

    private void createTimeLabel() {

        Skin skin = getSkin();

        // Create the label and name it for future updates
        Label timeLabel = LabelFactory.createLabel("0 s", skin, "defaultFont", null);
        timeLabel.setName("timeLabel");

        // Create a table to position the label at the top-right
        Table timeTable = new Table();
        timeTable.setFillParent(true); // Let the table span the whole stage
        timeTable.right().top().padRight(15).padTop(20);

        timeTable.add(timeLabel);

        // Add the table to the stage
        stage.addActor(timeTable);
    }

    private Table getTableByName(String name) {
        return stage.getRoot().findActor(name);
    }

    private Button getButtonByName(String name) {
        Table table = getTableByName(name);
        if (table != null && table.getChildren().size > 0) {
            Actor actor = table.getChildren().first();
            if (actor instanceof Button) {
                return (Button) actor;
            }
        }
        return null;
    }

    private void createJoystick() {
        Skin skin = getSkin();
        Texture joystickTexture = new Texture("joyStick.png");
        Image joystickImage = new Image(joystickTexture);

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = joystickImage.getDrawable();

        Drawable knobDrawable = skin.getDrawable("default-rect-pad");

        if (knobDrawable instanceof NinePatchDrawable) {
            NinePatchDrawable resizedKnob = new NinePatchDrawable((NinePatchDrawable) knobDrawable);
            resizedKnob.setMinWidth(80);
            resizedKnob.setMinHeight(50);
            touchpadStyle.knob = resizedKnob;
        } else {
            Gdx.app.error("Touchpad", "Unsupported drawable type: " + knobDrawable.getClass().getName());
        }
        
        
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 140, 140);
        stage.addActor(touchpad);

        Gdx.input.setInputProcessor(stage);
    }

    private void checkGameOver() {
        if (isMultiplayer && !hasSentFinishData && lobby != null) {
            hasSentFinishData = true;

            double time = 0;
            int score = 0;

            // Get the player's score and playtime
            if (LevelController.getInstance().getLevel() != null &&
                LevelController.getInstance().getLevel().getTimer() != null) {
                time = LevelController.getInstance().getLevel().getTimer().getTime();
                score = controller.getScore();
            }

            String uid = game.getCurrentUser().uid;
            MultiplayerManager manager = game.getMultiplayerManager();

            // Set player finish score and time)
            manager.setPlayerFinishData(lobby.lobbyId, uid, score, (long) time,
                new MultiplayerManager.Callback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        // Call endGame after successfully setting player finish data
                        manager.endGame(lobby.lobbyId, uid, new MultiplayerManager.Callback<Void>() {
                            @Override
                            public void onSuccess(Void r) {
                                Gdx.app.postRunnable(() ->
                                    game.setView(new MultiplayerScoreboardView(lobby))
                                );
                            }

                            @Override
                            public void onError(Exception e) {
                                System.err.println("EndGame failed: " + e.getMessage());
                                Gdx.app.postRunnable(() ->
                                    game.setView(new MultiplayerScoreboardView(lobby))
                                );
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Gdx.app.postRunnable(() ->
                            game.setView(new MultiplayerScoreboardView(lobby))
                        );
                    }
                }
            );
        } else {
            Gdx.app.postRunnable(() -> {
                HintUtil.setHint(controller.getLevel());
                if (controller.isCompleted()) {
                    game.setView(new GameOverView(controller.getLevel().levelNumber, true));
                } else {
                    game.setView(new GameOverView(controller.getLevel().levelNumber, false));
                }
            });
        }
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        if (controller != null) {
            controller.dispose();
            controller = null;
        }
        LevelController.resetInstance();
        pauseMenu = null;
        super.dispose();
    }
}
