package io.github.RangoUnchained.Controllers;

import io.github.RangoUnchained.Model.Systems.SystemManager;
import io.github.RangoUnchained.Model.level.GameLevel;

public class LevelController {

    private static LevelController levelController;
    
    private GameLevel level;
    private SystemManager systemManager;
    private LevelController () {
    }

    /* Hvordan skal vi legge til height og width. (Og sette det til spirte og body)
    * Dependencies:
    * - Simple burde bli kalt av Physics
    * - Simple : Height og width (Hvor skal det bli implementert)
    *
    * - Entity : Avhengig
    * */


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

    public void handleSpawnRequests() {
        for (PhysicsSystem.SpawnRequest request : physicsSystem.getSpawnRequests()) {
            BallEntity newBall = EntityFactory.createBallEntity(
                request.x, request.y, request.radius, request.spritePath, physicsSystem.getWorld(),
            request.timesPopped, request.velocity);

            physicsSystem.addEntity(newBall);
        }
        physicsSystem.clearSpawnRequests();

    }

    public void removeEntities() {
        for (Entity e : physicsSystem.getRemovalQueue()) {
            entities.remove(e);

            Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
            if (body != null) {
                world.destroyBody(body);
            }
        }
        physicsSystem.clearRemovalQueue();
    }

    // Skal ta inn JSON etterhvert (tar inn info om hvilke entiteter vi vil ha på hvert level)
    // Initializes systems with entities
    public void initializeSystems(int levelNumber) {
        level = new GameLevel(levelNumber);
        SystemManager systemManager = new SystemManager();
        System.out.println("Initialiserte riktig");
    }

    /**updates all entities with the appropiate systems */
    public void update() {
        systemManager.update(level.getEntities());
    }

    public World getWorld() {
        return world;
    }

    public LifeTimeSystem getLifeTimeSystem() {
        return lifeTimeSystem;
    }
}
