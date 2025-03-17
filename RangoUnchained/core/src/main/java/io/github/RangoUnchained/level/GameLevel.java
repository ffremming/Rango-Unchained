package io.github.RangoUnchained.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

public class GameLevel {
    private ArrayList<Object> entities;  // The actual game entities
    private MetaData metaData;

    private GameLevel(){}

    /**
     * Loads a game level from a file at the specified path.
     * The JSON data is used to create the entities via the EntityFactory.
     *
     * @param path The path to the level file.
     * @return A new GameLevel object created from the file data.
     */
    public static GameLevel loadLevel(String path) {
        Json json = new Json();
        FileHandle file = Gdx.files.internal("levels/" + path);

        // Deserialize JSON into the GameLevel object (just names and positions for entities)
        GameLevel level = json.fromJson(GameLevel.class, file.readString());

        // Log level name for testing
        Gdx.app.log("JSON_testing", "levelName: " + level.metaData.levelName);



        for (EntityData entityData : level.entitiesData) {
            Gdx.app.log("JSON_testing", "Name: " + entityData.name);
            Gdx.app.log("JSON_testing", "Position: (" + entityData.position.x + ", " + entityData.position.y + ")");

            //TODO FACTORY
            // Use the factory to create the actual entity based on the name
            //Object entity = EntityFactory.createEntity(entityData);
            //createdEntities.add(entity);
        }
        //TODO set/add after the factory is done
        //level.entities = createdEntities;

        return level;
    }

    /**
     * Clears all entities in the current level.
     * This removes all the entities from the level's entity list.
     */
    public void clearLevel() {
        entities.clear();
    }

    /**
     * Adds a new entity to the level.
     *
     * @param entity The entity to add to the level.
     * @return true if the entity was successfully added.
     */
    public boolean addEntity(Object entity) {
        entities.add(entity);
        return true;
    }

    /**
     * Gets the list of entities in the current level.
     *
     * @return A list of entities currently in the level.
     */
    public ArrayList<Object> getEntities() {
        return entities;
    }

    /**
     * Class for holding the basic entity data needed for JSON (name and position).
     */
    public static class EntityData {
        public String name;
        public Position position;

        public static class Position {
            public int x;
            public int y;
        }
    }

    /**
     * The simple entity data loaded from JSON.
     * This contains just the name and position of the entities.
     */
    public ArrayList<EntityData> entitiesData;

    /**
     * Class representing the level's metadata (levelName, difficulty, theme, multiplayer).
     */
    public static class MetaData {
        public String levelName;
        public int difficulty;
        public String theme;
        public boolean multiplayer;
    }
}
