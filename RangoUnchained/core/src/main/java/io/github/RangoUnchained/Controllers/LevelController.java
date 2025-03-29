package io.github.RangoUnchained.Controllers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.PhysicsSystem;
import io.github.RangoUnchained.Model.level.RemovalQueue;
import io.github.RangoUnchained.Model.level.SpawnQueue;
import io.github.RangoUnchained.Model.Systems.System;
import io.github.RangoUnchained.Model.Systems.SystemManager;
import io.github.RangoUnchained.Model.contactListener.ContactStrategies;
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
       removalQueue.addRemovalRequest(entity);
    }

    public void handleSpawnRequests(float xPos ,float yPos,int width, int height, String name, Vector2 velocity) {

            spawnQueue.addSpawnRequest(xPos, yPos, width, width, name, velocity, getWorld());
    }


    public void excecuteSpawnQueue() {
        level.spawn(spawnQueue.retrieveSpawningEntities());
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
    }

    /**updates all entities with the appropiate systems */
    public void update() {
        systemManager.update(level.getEntities());
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
}
