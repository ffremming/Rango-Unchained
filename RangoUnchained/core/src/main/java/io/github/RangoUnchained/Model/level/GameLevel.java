package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;

/**
 * Represents a game level, containing metadata and entities.
 * This class handles loading levels from JSON files and managing game entities.
 */
public class GameLevel {
    private ArrayList<Entity> entities = new ArrayList<>();
    private World world;
    private LevelData.MetaData metaData;
    private ArrayList<LevelData.EntityData> entitiesData;
    public ScoreManager scoreManager = new ScoreManager();

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
        Json json = new Json();
        FileHandle file = Gdx.files.internal("levels/level" + number+".json");

        LevelData levelData = json.fromJson(LevelData.class, file.readString());

        if (levelData.entitiesData == null) {
            Gdx.app.log("JSON_Error", "No entitiesData found in JSON file.");
            levelData.entitiesData = new ArrayList<>();
        }

        Gdx.app.log("JSON_testing", "levelName: " + levelData.metaData.number);

        for (LevelData.EntityData entityData : levelData.entitiesData) {
            Gdx.app.log("JSON_testing", "Name: " + entityData.name);
            Gdx.app.log("JSON_testing", "Position: (" + entityData.position.x + ", " + entityData.position.y + ")");

            Entity entity = EntityFactory.createEntity(entityData.position.x, entityData.position.y, entityData.name, LevelController.getInstance().getWorld(),new Vector2(0,0));
            if (entity!= null){
                entities.add(entity);
            }
        }
    }

    public World getWorld() {
        return world;
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


    public void addEntities(ArrayList<Entity> spawningEntities) {
        entities.addAll(spawningEntities);
    }

    public void removeEntities(ArrayList<Entity> removalEntities) {

        for (Entity e : removalEntities){


            Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
            if (body != null) {
                world.destroyBody(body);
            }
        }

        entities.removeAll(removalEntities);
    }
}
