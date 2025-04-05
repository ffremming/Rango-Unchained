package io.github.RangoUnchained.Model.level;

import java.util.ArrayList;

import io.github.RangoUnchained.Model.Components.BallComponent;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;

public class CheckpointHandler {

    private static float checkpointCounter = 0;

    /**
     * Writes state of entities to json, including backup file to prevent errors due to corrupt files.
     *
     * Writes every 3 seconds to files in local memory,
     * meaning it will not be written to project filepath.
     *
     * @param delta the amount of time passed since last call
     */
    public static void checkPoint(float delta, ArrayList<EntityData> entitiesData, ArrayList<Entity> entities,
            int levelNumber, int score, double time) {

        // If counter >= 3, write to JSON, else count and continue
        if (checkpointCounter + delta < 3) {
            checkpointCounter += delta;
            return;
        }

        ArrayList<LevelData.EntityData> entitiesDataCheckPoint = new ArrayList<>();

        // Add every entity that is not a player of ball
        for (EntityData entityData : entitiesData) {
            if (entityData.name.startsWith("Player") || entityData.name.startsWith("Ball")) {
                continue;
            }
            entitiesDataCheckPoint.add(entityData);
        }
        // Add every ball and player entity
        for (Entity entity : entities) {
            if (entity instanceof BallEntity) {
                entitiesDataCheckPoint.add(makeBallEntity(entity));
            }
            if (entity instanceof PlayerEntity) {
                entitiesDataCheckPoint.add(makePlayerEntity(entity));
            }
        }

        // Set metaData
        LevelData.MetaData metaData = new LevelData.MetaData();
        metaData.progress = 1; // Set to on-going
        metaData.levelnr = levelNumber;
        metaData.score = score;
        metaData.time = time;

        LevelData levelData = new LevelData();
        levelData.metaData = metaData;
        levelData.entitiesData = entitiesDataCheckPoint;

        // Write levelData to file and backup file.
        GameFileHandler.getInstance().writeLevelDataToLocalFile(levelData, "levels/checkpoint.json");
        GameFileHandler.getInstance().writeLevelDataToLocalFile(levelData, "levels/checkpointBackup.json");

        // After writing, reset counter.
        checkpointCounter = 0;
    }


    /**
     * called when the game is started, to potentially reset the checkpoint
     */
    public static void initializeCheckpoint(int levelNumber){
        if (GameFileHandler.inProgresslevelnumber() != levelNumber){
            resetCheckpoint();
        }
    }

    /**
     * Resets the checkpoint - should be called when the game is started, completed or ended.
     */
    public static void resetCheckpoint(){
        checkpointCounter = 0;
        GameFileHandler.getInstance().resetCheckpointFile();
    }


    /**
     * Creates an EntityData object from  Entity object
     *
     * @param entity Current entity being mapped
     * @return EntityData object of the corresponding entity argument
     */
    public static EntityData makeBallEntity(Entity entity) {
        EntityData entityData = new EntityData();
        // Fetch needed components
        BodyComponent body = (BodyComponent) entity.getComponent(BodyComponent.class);
        StatComponent stat = (StatComponent) entity.getComponent(StatComponent.class);
        SpriteComponent sprite = (SpriteComponent) entity.getComponent(SpriteComponent.class);

        BallComponent ballComp = (BallComponent) entity.getComponent(BallComponent.class);

        // Set the name property
        String size = stat.getTimesPopped() == 0 ? "Big" : stat.getTimesPopped() == 1 ? "Medium" : "Small";

        String recipe = "Ball: "+ ballComp.getTypeName() + " " +size;
        entityData.name = recipe;
        // Set the position property
        EntityData.Position entityPosition = new EntityData.Position();
        entityPosition.x = sprite.getSprite().getX();
        entityPosition.y = sprite.getSprite().getY();
        entityData.position = entityPosition;

        // Set the velocity property
        entityData.velocity = body.getBody().getLinearVelocity();
        return entityData;
    }

    /**
     * Creates an EntityData object from Entity object
     *
     * @param entity Current entity being mapped
     * @return EntityData object of the corresponding entity argument
     */
    public static EntityData makePlayerEntity(Entity entity) {
        EntityData entityData = new EntityData();
        // Fetch needed components
        BodyComponent body = (BodyComponent) entity.getComponent(BodyComponent.class);
        SpriteComponent sprite = (SpriteComponent) entity.getComponent(SpriteComponent.class);
        HealthComponent health = (HealthComponent) entity.getComponent(HealthComponent.class);


        // Set the name property
        entityData.name = "Player" + "-" + sprite.getPath();

        // Set the position property
        EntityData.Position entityPosition = new EntityData.Position();
        entityPosition.x = sprite.getSprite().getX();
        entityPosition.y = 150;
        entityData.position = entityPosition;
        entityData.health = health.getHealth();

        return entityData;
    }
}
