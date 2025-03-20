package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.level.ScoreManager;

/**
 * Represents a game level, containing metadata and entities.
 * This class handles loading levels from JSON files and managing game entities.
 */
public class GameLevel {
    private ArrayList<Entity> entities = new ArrayList<>();
    private MetaData metaData;
    private ArrayList<EntityData> entitiesData;
    public ScoreManager scoreManager = new ScoreManager();

    private GameLevel() {}

    /**
     * Loads a game level from a JSON file.
     *
     * @param path The relative path to the level file within the "levels" directory.
     * @return A new GameLevel instance populated with metadata and entities.
     */
    public static GameLevel loadLevel(String path) {
        Json json = new Json();
        FileHandle file = Gdx.files.internal("levels/" + path);

        GameLevel level = json.fromJson(GameLevel.class, file.readString());

        if (level.entitiesData == null) {
            Gdx.app.log("JSON_Error", "No entitiesData found in JSON file.");
            level.entitiesData = new ArrayList<>();
        }

        Gdx.app.log("JSON_testing", "levelName: " + level.metaData.number);

        for (EntityData entityData : level.entitiesData) {
            Gdx.app.log("JSON_testing", "Name: " + entityData.name);
            Gdx.app.log("JSON_testing", "Position: (" + entityData.position.x + ", " + entityData.position.y + ")");

            Entity entity = EntityFactory.createPlayerEntity(entityData.position.x, entityData.position.y, entityData.name);
            level.entities.add(entity);
        }
        return level;
    }

    /**
     * Removes all entities from the level.
     */
    public void clearLevel() {
        entities.clear();
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

    /**
     * Represents entity data used for JSON deserialization.
     */
    public static class EntityData {
        public String name;
        public Position position;

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
        public int number;
        public String theme;
        public boolean completed;
        public boolean multiplayer;
    }
}
