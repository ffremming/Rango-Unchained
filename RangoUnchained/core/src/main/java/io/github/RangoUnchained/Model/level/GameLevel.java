package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
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
    public ScoreManager scoreManager = new ScoreManager(this);
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
        levelNumber = number;
        GameFileHandler.getInstance().setLevelNumber(number);
        /*Gdx.files.local("levels/checkpoint.json").delete();
        Gdx.files.local("levels/checkpointBackup.json").delete();
        Gdx.files.local("levels/progress.json").delete();*/


        LevelData levelData = GameFileHandler.getInstance().readLevelDataFromLocal("levels/checkpoint.json");

        if (levelData == null) {
            levelData = GameFileHandler.getInstance().readLevelDataFromLocal("levels/checkpointBackup.json");
        }

        if (levelData == null) {
            levelData = new GameLevel.LevelData();
            levelData.metaData = new LevelData.MetaData();
            levelData.entitiesData = new ArrayList<>();
        }

        if (levelData.entitiesData == null) {
            levelData.entitiesData = new ArrayList<>();
        }
        if (levelData.metaData == null) {
            levelData.metaData = new LevelData.MetaData();
        }

        System.out.println("LEVELPROGRESS: " + levelData.metaData.progress + " | LEVELNUMBER METADATA " + levelData.metaData.levelnr + " | chosen levelnumber : " + number);
        System.out.println(levelData.metaData.progress);
        if (levelData.metaData.progress == 0 || levelData.metaData.levelnr != number) {
            LevelData fallbackData = GameFileHandler.getInstance().readLevelDataFromAssets("levels/level" + number + ".json");
            if (fallbackData != null) {
                levelData = fallbackData;
            }
            if (levelData.entitiesData == null) {
                levelData.entitiesData = new ArrayList<>();
            }
        }

        this.entitiesData = levelData.entitiesData;

        Gdx.app.log("JSON_testing", "levelName: " + levelData.metaData.levelnr);

        scoreManager.setScore(levelData.metaData.score);
        timer.setTime(levelData.metaData.time);
        spawn(levelData.entitiesData, levelData.metaData.levelnr);
    }


    public void checkpoint(float delta){
        CheckpointHandler.checkPoint(delta, entitiesData, entities, levelNumber, scoreManager.getScore(), timer.getTime());
    }

    public LevelData.MetaData getMetaData() {
        return metaData;
    }


    public Timer getTimer() {
        return this.timer;
    }
    public int getScore() {
        return this.scoreManager.getScore();
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
            public Dimension dimension;
            public int health;
            public Vector2 velocity;
            public TypeInfo typeInfo;

            /**
             * Represents a 2D position with x and y coordinates.
             */
            public static class Dimension {
                public float x;
                public float y;
                public float width;
                public float height;
            }

            public static class TypeInfo {
                public String type;
                public String subType;
                public int size;
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
        World world = LevelController.getInstance().getWorld();
        if (world == null) return;

        for (Entity e : removalEntities){

            if (e.getComponent(BodyComponent.class)!= null){
                Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
                if (body != null) {
                    world.destroyBody(body);
                }
            }
        }

        entities.removeAll(removalEntities);
    }

    public void spawn(ArrayList<EntityData> spawningEntities, int levelNumber) {
        LevelData levelData = new LevelData();
        levelData.metaData = new LevelData.MetaData();
        levelData.metaData.levelnr = levelNumber;
        for (EntityData data: spawningEntities){
            Gdx.app.log("JSON_testing", "Name: " + data.name);
            Gdx.app.log("JSON_testing", "Position: (" + data.dimension.x + ", " + data.dimension.y + ")");
            Vector2 velocity = data.velocity == null ? new Vector2() : data.velocity;
            Entity entity = EntityFactory.create(data,levelData);
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
        try {
            removeEntities(new ArrayList<>(entities));
        } catch (Exception e) {
            System.err.println("Error during GameLevel.dispose(): " + e.getMessage());
        }
    }

    public void initializeCheckpoint() {
        CheckpointHandler.initializeCheckpoint(levelNumber);
    }

    public void resetCheckpoint() {
        CheckpointHandler.resetCheckpoint();
    }
}
