package io.github.RangoUnchained.Controllers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategies;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Systems.AudioSystem;
import io.github.RangoUnchained.Model.Systems.HealthSystem;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.PhysicsSystem;
import io.github.RangoUnchained.Model.Systems.PowerUpSystem;
import io.github.RangoUnchained.Model.level.RemovalQueue;
import io.github.RangoUnchained.Model.level.SpawnQueue;
import io.github.RangoUnchained.Model.Systems.System;
import io.github.RangoUnchained.Model.Systems.SystemManager;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Model.level.GameLevel;

public class LevelController {

    private static LevelController levelController;

    public boolean completed = false;
    private GameLevel level;
    private SystemManager systemManager;
    private SpawnQueue spawnQueue;
    private RemovalQueue removalQueue;
    private boolean isActive = true;


    private LevelController () {
    }

    public static LevelController getInstance(){
        if (levelController == null) {
            levelController = new LevelController();
        }
        return levelController;
    }

    public static void resetInstance() {
        if (levelController != null) {
            levelController.dispose();
            levelController = null;
        }
    }

    /** adds entity to level */
    public void addEntity(Entity e) {
        level.addEntity(e);
    }

    public void handleRemovalRequests(Entity entity) {
       removalQueue.addRemovalRequest(entity);
    }

    public void handleSpawnRequests(float xPos ,float yPos,int width, int height, String name, Vector2 velocity) {
        spawnQueue.addSpawnRequest(xPos, yPos, width, width, name, velocity, getWorld());
    }


    public void excecuteSpawnQueue() {
        level.spawn(spawnQueue.retrieveSpawningEntities(),1);
    }

    public void excecuteRemovelQueue() {
        level.removeEntities(removalQueue.getRemovalEntities(getWorld()));
    }

    // Initializes systems with entities
    public void initializeSystems(int levelNumber) {
        this.isActive = true;
        this.systemManager = new SystemManager();
        this.level = new GameLevel(levelNumber);
        this.spawnQueue = new SpawnQueue();
        this.removalQueue = new RemovalQueue();
        ContactStrategies ContactStrategies = new ContactStrategies();

        getSystem(PhysicsSystem.class).setContactStrategies();
        getSystem(HealthSystem.class).setContactStrategies();
        getSystem(PowerUpSystem.class).setContactStrategies();

        level.initializeCheckpoint();
    }

    /**updates all entities with the appropiate systems */
    public void update(float delta) {

        excecuteRemovelQueue();
        excecuteSpawnQueue();
        checkpoint(delta);

        systemManager.update(level.getEntities(), delta);
        level.getTimer().update(delta);
    }

    public boolean isGameOver() {
        if (getLevel() == null) {
            return false;
        }
        if (hasDied()){
            level.resetCheckpoint();
            return true;
        }

        if (isCompleted()){
            int levelNumber = getLevel().levelNumber;
            if (levelNumber >= GameFileHandler.getInstance().getProgress()) {
                GameFileHandler.getInstance();
                GameFileHandler.setProgress(levelNumber+1);
            }
            level.resetCheckpoint();
            return true;
        }
        return false;
    }

    private boolean hasDied(){
        return (getPlayerHealth()<= 0);
    }

    public boolean isCompleted(){
        if (level == null) {
            return false;
        }
        int levelNumber = getLevel().levelNumber;
        if (levelNumber == 0 &&! completed){
            return false;
        }
        return (level.getEntity(BallEntity.class).size() == 0);
    }

    public void step(float f, int i, int j) {
        systemManager.getWorld().step(f, i, j);
    }

    public ArrayList<Entity> getEntities(){
        return level.getEntities();
    }

    public World getWorld() {
        return systemManager.getWorld();
    }

    public AudioSystem getAudioSystem(){return systemManager != null ? systemManager.getAudioSystem() : null;}

    public <T extends System> T getSystem(Class<T> systemClass) {
        if (!isActive || systemManager == null) return null;
        return systemManager.getSystem(systemClass);
    }

    /**methdod for shooting, called from view */
    public void handleShoot() {
        if (level == null || level.getEntities() == null) {
            return;
        }

        InputSystem inputSystem = getSystem(InputSystem.class);
        if (inputSystem != null) {
            inputSystem.handleShoot(level.getEntities());
        }
    }

    public int getScore() {
        if (level == null) {
            return 0;
        }
        return level.scoreManager.getScore();
    }

    public GameLevel getLevel() {
        return this.level;
    }

    public int getPlayerHealth(){
        if (level == null){return 0;}
        ArrayList<PlayerEntity> entities= level.getEntity(PlayerEntity.class);
        if (entities.size()<=0){return 0;}

        int health = ((HealthComponent)(entities.get(0).getComponent(HealthComponent.class))).getHealth();
        return health;
    }

    public ArrayList<Integer> getPlayerActivePowerup(){
        ArrayList<Integer> list = new ArrayList<>();
        if (level == null){return list;}
        ArrayList<PlayerEntity> entities= level.getEntity(PlayerEntity.class);
        if (entities.size()<=0){return list;}

        list.addAll(((PowerUpComponent)(entities.get(0).getComponent(PowerUpComponent.class))).getActivePowerUps().keySet());
        return list;
    }

    public void checkpoint(float delta) {
        level.checkpoint(delta);
    }

    public void dispose() {
        isActive = false;

        if (level != null) {
            level.dispose();
            level = null;
        }
        if (systemManager != null) {
            systemManager.dispose();
            systemManager = null;
        }
    }
}
