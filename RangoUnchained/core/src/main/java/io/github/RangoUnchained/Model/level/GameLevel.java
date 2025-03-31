package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Views.Utils.Constants;

/**
 * Represents a game level, containing metadata and entities.
 * This class handles loading levels from JSON files and managing game entities.
 */
public class GameLevel {
    private ArrayList<Entity> entities = new ArrayList<>();
    private LevelData.MetaData metaData;
    private ArrayList<LevelData.EntityData> entitiesData;
    private float checkpointCounter = 0;
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
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle file = Gdx.files.internal("levels/level" + number+".json");

        LevelData levelData = json.fromJson(LevelData.class, file.readString());
        metaData = levelData.metaData;
        entitiesData = levelData.entitiesData;

        if (metaData.progress == 1) {
            file = Gdx.files.internal("levels/checkpoint.json");
            levelData = json.fromJson(LevelData.class, file.readString());
            metaData = levelData.metaData;
            entitiesData = levelData.entitiesData;
        } else {
            metaData.progress = 1; // Set to on-going
            try (FileWriter fileWriter = new FileWriter("assets/levels/level" + number + ".json")) {
                fileWriter.write(json.prettyPrint(levelData));
            } catch (IOException e) {
                System.out.println("Could not write checkpoint to json file");
            }
        }

        if (levelData.entitiesData == null) {
            Gdx.app.log("JSON_Error", "No entitiesData found in JSON file.");
            levelData.entitiesData = new ArrayList<>();
        }

        Gdx.app.log("JSON_testing", "levelName: " + levelData.metaData.number);

        spawn(levelData.entitiesData);
    }

    public void checkpoint(float delta) {
        if (checkpointCounter < 3) {
            checkpointCounter += delta;
            System.out.println("Before checkpoint: " + checkpointCounter);
            return;
        }

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        LevelData levelData = new LevelData();

        ArrayList<LevelData.EntityData> entitiesData = new ArrayList<>();

        for (EntityData entityData : this.entitiesData) {
            if (entityData.name.startsWith("Player") || entityData.name.startsWith("Ball")) {
                continue;
            }
            entitiesData.add(entityData);
        }

        for (Entity entity : entities) {
            //TODO: Need a way to add the walls/obstacles.
            if (entity instanceof BallEntity) {
                entitiesData.add(writeBallEntity(entity));
            }
            if (entity instanceof PlayerEntity) {
                entitiesData.add(writePlayerEntity(entity));
            }
        }

        levelData.entitiesData = entitiesData;
        levelData.metaData = metaData;

        try (FileWriter fileWriter = new FileWriter("assets/levels/checkpoint.json")) {
            fileWriter.write(json.prettyPrint(levelData));
        } catch (IOException e) {
            System.out.println("Could not write checkpoint to json file");
        }

        checkpointCounter = 0;
    }

    public LevelData.MetaData writeMetaData() {
        return metaData;
    }

    public EntityData writeBallEntity(Entity entity) {
        EntityData entityData = new EntityData();
        // Fetch needed components
        BodyComponent body = (BodyComponent) entity.getComponent(BodyComponent.class);
        StatComponent stat = (StatComponent) entity.getComponent(StatComponent.class);
        SpriteComponent sprite = (SpriteComponent) entity.getComponent(SpriteComponent.class);

        // Set the name property
        entityData.name = stat.getTimesPopped() == 0 ? "BallBig" : stat.getTimesPopped() == 1 ? "BallMedium" : "BallSmall";

        // Set the position property
        EntityData.Position entityPosition = new EntityData.Position();
        entityPosition.x = sprite.getSprite().getX();
        entityPosition.y = sprite.getSprite().getY();
        entityData.position = entityPosition;

        // Set the velocity property
        entityData.velocity = body.getBody().getLinearVelocity();
        return entityData;
    }

    public EntityData writePlayerEntity(Entity entity) {
        EntityData entityData = new EntityData();
        // Fetch needed components
        BodyComponent body = (BodyComponent) entity.getComponent(BodyComponent.class);
        SpriteComponent sprite = (SpriteComponent) entity.getComponent(SpriteComponent.class);


        // Set the name property
        entityData.name = "Player";

        // Set the position property
        EntityData.Position entityPosition = new EntityData.Position();
        entityPosition.x = sprite.getSprite().getX();
        entityPosition.y = sprite.getSprite().getY();
        entityData.position = entityPosition;

        return entityData;
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
            public int number;
            public String theme;
            public boolean completed;
            public boolean multiplayer;
            public int progress;
        }
    }


    public void addEntities(ArrayList<Entity> spawningEntities) {
        entities.addAll(spawningEntities);
    }

    public void removeEntities(ArrayList<Entity> removalEntities) {

        for (Entity e : removalEntities){

            Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
            if (body != null) {
                LevelController.getInstance().getWorld().destroyBody(body);
            }
        }

        entities.removeAll(removalEntities);
    }

    public void spawn(ArrayList<EntityData> spawningEntities) {
        for (EntityData data: spawningEntities){
            Gdx.app.log("JSON_testing", "Name: " + data.name);
            Gdx.app.log("JSON_testing", "Position: (" + data.position.x + ", " + data.position.y + ")");
            Vector2 velocity = data.velocity == null ? new Vector2() : data.velocity;
            Entity entity = EntityFactory.createEntity(data.position.x, data.position.y, data.name, LevelController.getInstance().getWorld(),velocity);
            if (entity!= null){
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
}
