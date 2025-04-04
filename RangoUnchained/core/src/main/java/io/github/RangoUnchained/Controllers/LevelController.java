package io.github.RangoUnchained.Controllers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategies;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Systems.HealthSystem;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.PhysicsSystem;
import io.github.RangoUnchained.Model.Systems.PowerUpSystem;
import io.github.RangoUnchained.Model.level.RemovalQueue;
import io.github.RangoUnchained.Model.level.SpawnQueue;
import io.github.RangoUnchained.Model.Systems.System;
import io.github.RangoUnchained.Model.Systems.SystemManager;
import io.github.RangoUnchained.Model.level.GameLevel;

public class LevelController {

    private static LevelController levelController;

    private GameLevel level;
    private SystemManager systemManager;
    private SpawnQueue spawnQueue;
    private RemovalQueue removalQueue;


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
            levelController = null;
        }
    }

    /** clear systems (TODO) */
    public void clearSystems() {
        level.clear();
    }

    /** should be handled differently */

    /** adds entity to level */
    public void addEntity(Entity e) {
        level.addEntity(e);
    }

    public void handleRemovalRequests(Entity entity) {
        // 
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

    // Skal ta inn JSON etterhvert (tar inn info om hvilke entiteter vi vil ha på hvert level)
    // Initializes systems with entities
    public void initializeSystems(int levelNumber) {
        systemManager = new SystemManager();
        level = new GameLevel(levelNumber);
        spawnQueue = new SpawnQueue();
        removalQueue = new RemovalQueue();
        ContactStrategies ContactStrategies = new ContactStrategies();

        getSystem(PhysicsSystem.class).setContactStrategies();
        getSystem(HealthSystem.class).setContactStrategies();
        getSystem(PowerUpSystem.class).setContactStrategies();
    }

    /**updates all entities with the appropiate systems */
    public void update(float delta) {
        systemManager.update(level.getEntities());
        level.getTimer().update(delta);
    }

    public void step(float f, int i, int j) {
        systemManager.getWorld().step(f, i, j);
    }

    public ArrayList<Entity> getEntities(){
        return level.getEntities();
    }

    public World getWorld() {
        return getSystem(PhysicsSystem.class).getWorld();
    }

    public <T extends System> T getSystem(Class<T> systemClass) {
        return systemManager.getSystem(systemClass);
    }

    /**methdo for shooting, called from view */
    public void handleShoot(){
        getSystem(InputSystem.class).handleShoot(level.getEntities());
    }

    public int getScore(){
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

}
