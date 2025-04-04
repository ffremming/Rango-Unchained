package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;

public class KillEnemyStrategy implements TutorialStepStrategy{

    int amountKills;
    String entityType;
    String message;
    float timeElapsed = 0;
    int maxTime = 0;

    public KillEnemyStrategy(String entityType, int amountKills,String message, int maxTime) {
        this.amountKills = amountKills;
        this.entityType = entityType;
        this.message = message;
        this.maxTime = maxTime;
    }

    @Override
    public void onEnter() {
       LevelController.getInstance().handleSpawnRequests(300, 300, 0, 0, entityType, new Vector2(1,7));
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