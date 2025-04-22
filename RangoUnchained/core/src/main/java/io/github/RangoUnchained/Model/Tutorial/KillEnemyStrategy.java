package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.Dimension;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.TypeInfo;

public class KillEnemyStrategy implements TutorialStepStrategy{

    int amountKills;
    EntityData entityData;
    String message;
    float timeElapsed = 0;
    int maxTime = 0;

    public KillEnemyStrategy(EntityData entityData, int amountKills,String message, int maxTime) {
        this.amountKills = amountKills;
        this.entityData = entityData;
        this.message = message;
        this.maxTime = maxTime;
    }

    @Override
    public void onEnter() {

       LevelController.getInstance().handleSpawnRequests(entityData);
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        timeElapsed += deltaTime;
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return (context.amountBallsKilled == amountKills) || (timeElapsed > maxTime);
    }

    @Override
    public String getMessage() {
        return message;
    }
}