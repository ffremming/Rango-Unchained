package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;

/**
 * Represents a game level, containing metadata and entities.
 * This class handles loading levels from JSON files and managing game entities.
 */
public class GameLevel {
    private ArrayList<Entity> entities = new ArrayList<>();
    private LevelData.MetaData metaData;
    private ArrayList<LevelData.EntityData> entitiesData;
    public ScoreManager scoreManager = new ScoreManager();
    public Timer timer = new Timer();
    public int levelNumber;

    public GameLevel(int number) {
        loadLevel(number);
    }

    /**
     * Loads a game level from a JSON file.
     *
     * @param number the identificator of the specific level.
     * @return A new GameLevel instance populated with metadata and entities.
     */
    public void loadLevel(int number) {
        System.out.println("DEBUG: Starting loadLevel with level number: " + number);
        levelNumber = number;
        GameFileHandler.getInstance().setLevelNumber(number);
        LevelData levelData;
    
        // Try to load the checkpoint file first.
        System.out.println("DEBUG: Attempting to load 'levels/checkpoint.json'");
        if (GameFileHandler.getInstance().makeLevelData("levels/checkpoint.json") != null) {
            levelData = GameFileHandler.getInstance().makeLevelData("levels/checkpoint.json");
            System.out.println("DEBUG: Successfully loaded 'levels/checkpoint.json'");
        } else {
            levelData = GameFileHandler.getInstance().makeLevelData("levels/checkpointBackup.json");
            System.out.println("DEBUG: 'levels/checkpoint.json' not found. Loaded 'levels/checkpointBackup.json' instead");
        }
    
        if (levelData == null) {
            System.out.println("DEBUG: LevelData is null, initializing new LevelData and MetaData.");
            levelData = new GameLevel.LevelData();
            levelData.metaData = new LevelData.MetaData();
        }
    
        // Save the entities data from the levelData.
        entitiesData = levelData.entitiesData;
    
        // Print metadata details for debugging.
        System.out.println("DEBUG: Level Metadata - progress: " + levelData.metaData.progress 
                + " | levelnr: " + levelData.metaData.levelnr 
                + " | chosen level number: " + number);
                
        // If the game was not in progress or the chosen level doesn't match the metadata, load the proper level.
        if (levelData.metaData.progress == 0 || levelData.metaData.levelnr != number) {
            System.out.println("DEBUG: Progress is 0 or level number mismatch. Loading 'levels/level" + number + ".json'");
            levelData = GameFileHandler.getInstance().makeLevelData("levels/level" + number + ".json");
            entitiesData = levelData.entitiesData;
        }
    
        // Check for null entitiesData.
        if (levelData.entitiesData == null) {
            Gdx.app.log("JSON_Error", "No entitiesData found in JSON file.");
            System.out.println("DEBUG: entitiesData is null, initializing new ArrayList.");
            levelData.entitiesData = new ArrayList<>();
        }
    
        Gdx.app.log("JSON_testing", "levelName: " + levelData.metaData.levelnr);
        System.out.println("DEBUG: levelName from metaData: " + levelData.metaData.levelnr);
        
        // If the metadata level number is 0, reload the level.
        if (levelData.metaData.levelnr == 0) {
            System.out.println("DEBUG: metaData number is 0, reloading 'levels/level" + number + ".json'");
            levelData = GameFileHandler.getInstance().makeLevelData("levels/level" + number + ".json");
            entitiesData = levelData.entitiesData;
        }
        
        // Set score and timer.
        System.out.println("DEBUG: Setting score to " + levelData.metaData.score);
        scoreManager.setScore(levelData.metaData.score);
        
        System.out.println("DEBUG: Setting timer to " + levelData.metaData.time);
        timer.setTime(levelData.metaData.time);
        
        // Spawn entities.
        System.out.println("DEBUG: Spawning entities with level number: " + levelData.metaData.levelnr);
        spawn(levelData.entitiesData, levelData.metaData.levelnr);
        
        System.out.println("DEBUG: Level load complete.");
    }
    

    public void checkpoint(float delta){
        CheckpointHandler.checkPoint(delta, entitiesData, entities, levelNumber, scoreManager.getScore(), timer.getTime());
    }


    public Timer getTimer() {
        return this.timer;
    }


    /**
     * Removes all entities from the level.
     */
    public void clear() {
        entities.clear();
        //TODO more logic? disposing?
    }

    /**
     * Adds a new entity to the level.
     *
     * @param entity The entity to add.
     * @return {@code true} if the entity was successfully added.
     */
    public boolean addEntity(Entity entity) {
        return entities.add(entity);
    }

    /**
     * Retrieves the list of entities currently in the level.
     *
     * @return A list of entities present in the level.
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }


    public static class LevelData {
        public MetaData metaData;
        public ArrayList<EntityData> entitiesData;

        /**
         * Represents entity data used for JSON deserialization.
         */
        public static class EntityData {
            public String name;
            public Position position;
            public int health;
            public Vector2 velocity;

            /**
             * Represents a 2D position with x and y coordinates.
             */
            public static class Position {
                public float x;
                public float y;
            }
        }

        /**
         * Represents metadata associated with a game level.
         */
        public static class MetaData {
            public String theme;
            public boolean completed;
            public boolean multiplayer;
            public int progress;
            public int levelnr;
            public int score;
            public double time;
        }
    }


    public void addEntities(ArrayList<Entity> spawningEntities) {
        entities.addAll(spawningEntities);
    }

    public void removeEntities(ArrayList<Entity> removalEntities) {

        for (Entity e : removalEntities){

            if (e.getComponent(BodyComponent.class)!= null){
                Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
                if (body != null) {
                    LevelController.getInstance().getWorld().destroyBody(body);
                }
            }
        }

        entities.removeAll(removalEntities);
    }

    public void spawn(ArrayList<EntityData> spawningEntities, int levelNumber) {
        for (EntityData data: spawningEntities){
            Gdx.app.log("JSON_testing", "Name: " + data.name);
            Gdx.app.log("JSON_testing", "Position: (" + data.position.x + ", " + data.position.y + ")");
            Vector2 velocity = data.velocity == null ? new Vector2() : data.velocity;
            Entity entity = EntityFactory.createEntity(data.position.x, data.position.y, data.name, LevelController.getInstance().getWorld(),velocity, data.health, levelNumber);
            if (entity != null){
                entities.add(entity);
            }
        }
    }

    /**
     * Returns a entity of the specified class type.
     *
     * @param <T> the type of the system
     * @param systemClass the class of the system to return
     * @return the system of the specified class type, or null if not found
     */
    public <T extends Entity> ArrayList<T> getEntity(Class<T> entityClass) {
        ArrayList<T> wantedEntities = new ArrayList<T>();
        for (Entity entity : entities) {
            if (entityClass.isInstance(entity)) {
                wantedEntities.add(entityClass.cast(entity));
            }
        }
        return wantedEntities;
    }

    public void dispose() {
        removeEntities(entities);
    }

    public void initializeCheckpoint() {
        CheckpointHandler.initializeCheckpoint(levelNumber);
    }

    public void resetCheckpoint() {
        CheckpointHandler.resetCheckpoint();
    }
}
