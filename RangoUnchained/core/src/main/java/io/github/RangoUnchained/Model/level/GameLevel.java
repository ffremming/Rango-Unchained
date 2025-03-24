package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.MovementSystem;
import io.github.RangoUnchained.Model.Systems.PhysicsSystem;
import io.github.RangoUnchained.Model.Systems.Systems;

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

    private ArrayList<Systems> systems = new ArrayList<>();

    public GameLevel(int number) {
        initializeSystems();
        loadLevel(number);
        loadSystemsWithEntities();
    }

    /**
     * Loads a game level from a JSON file.
     *
     * @param number the identificator of the specific level.
     * @return A new GameLevel instance populated with metadata and entities.
     */
    public void loadLevel(int number) {
        System.out.println("run");
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

            Entity entity = EntityFactory.createEntity(entityData.position.x, entityData.position.y, entityData.name, world);
            if (entity!= null){
                entities.add(entity);
            }
        }
    }

    public void update(){
        for (Systems system: systems) {
            system.update();
        }
    }

    public void addSystem(Systems system){
        systems.add(system);
    }

    private void initializeSystems(){

        MovementSystem movementSystem = new MovementSystem();
        PhysicsSystem physicsSystem = new PhysicsSystem(-10);
        InputSystem inputSystem = new InputSystem();

        world = physicsSystem.getWorld();

        systems.add(movementSystem);
        systems.add(physicsSystem);
        systems.add(inputSystem);
    }

    private void loadSystemsWithEntities(){
        for (Systems system: systems) {
            for (Entity entity:entities){
                system.addEntity(entity);
            }
        }
    }

    private void addEntityToSystem(Entity entity){
        for (Systems system: systems) {
                system.addEntity(entity);
        }
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
        addEntityToSystem(entity);
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
}
