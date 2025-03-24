package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;

public class GameLevel {
    private ArrayList<Entity> entities;  // The actual game entities
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
        Gdx.app.log("JSON_testing", "levelName: " + level.metaData.number);

        for (EntityData entityData : level.entitiesData) {
            Gdx.app.log("JSON_testing", "Name: " + entityData.name);
            Gdx.app.log("JSON_testing", "Position: (" + entityData.pos.x + ", " + entityData.pos.y + ")");

            Entity entity = EntityFactory.createPlayerEntity(entityData.pos.x, entityData.pos.y, entityData.name);
            level.entities.add(entity);
        }
        return level;
    }

    /**
     * Clears all entities in the current level.
     * This removes all the entities from the level's entity list.
     */
    public void clear() {
        entities.clear();
        //TODO more logic? disposing?
    }

    /**
     * Adds a new entity to the level.
     *
     * @param entity The entity to add to the level.
     * @return true if the entity was successfully added.
     */
    public boolean addEntity(Entity entity) {
        entities.add(entity);
        return true;
    }

    /**
     * Gets the list of entities in the current level.
     *
     * @return A list of entities currently in the level.
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Class for holding the basic entity data needed for JSON (name and position).
     */
    public static class EntityData {
        public String name;
        public Position pos;

        public static class Position {
            public float x;
            public float y;
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
        public int number;
        public String theme;
        public boolean multiplayer;
    }
}
