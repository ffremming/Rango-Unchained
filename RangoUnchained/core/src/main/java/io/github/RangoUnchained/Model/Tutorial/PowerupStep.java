package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.Dimension;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.TypeInfo;

public class PowerupStep implements TutorialStepStrategy{

    String powerupType;
    String message;
    float timeElapsed = 0;
    int maxTime = 0;

    public PowerupStep(String powerupType,String message, int maxTime) {
        this.powerupType = powerupType;
        this.message = message;
        this.maxTime = maxTime;
    }

    @Override
    public void onEnter() {
        EntityData data3 = new EntityData();
        data3.dimension = new Dimension();
        data3.dimension.x = 300;
        data3.dimension.y = 300;
        data3.name = "SpeedPowerUp";
        data3.typeInfo = new TypeInfo();
        data3.typeInfo.type = "powerup";
        data3.typeInfo.subType = "speed";
        data3.typeInfo.size = 2;
        data3.velocity = new Vector2(2,2);

       LevelController.getInstance().handleSpawnRequests(data3);
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        timeElapsed += deltaTime;
        
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return (timeElapsed > maxTime);
    }

    @Override
    public String getMessage() {
        return message;
    }
}