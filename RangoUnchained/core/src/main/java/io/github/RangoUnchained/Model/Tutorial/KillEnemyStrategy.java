package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;

public class KillEnemyStrategy implements TutorialStepStrategy{

    int amountKills;
    String entityType;

    public KillEnemyStrategy(String entityType, int amountKills){
        this.amountKills = amountKills;
        this.entityType = entityType;
    }

    @Override
    public void onEnter() {
       LevelController.getInstance().handleSpawnRequests(300, 300, 0, 0, entityType, new Vector2(1,7));
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
       
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return context.amountBallsKilled == amountKills;
    }

    @Override
    public String getMessage() {
        return "shoot the enemy to split it";
    }
}