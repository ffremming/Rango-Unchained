package io.github.RangoUnchained.Controllers;

import io.github.RangoUnchained.Model.Systems.SystemManager;
import io.github.RangoUnchained.Model.level.GameLevel;

public class LevelController {

    private static LevelController levelController;
    
    private GameLevel level;
    private SystemManager systemManager;
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
}
