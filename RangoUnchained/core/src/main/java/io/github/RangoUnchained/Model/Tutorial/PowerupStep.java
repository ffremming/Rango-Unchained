package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;

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
       LevelController.getInstance().handleSpawnRequests(300, 300, 0, 0, powerupType, new Vector2(1,7));
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