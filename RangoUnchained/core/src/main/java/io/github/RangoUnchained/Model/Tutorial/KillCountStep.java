package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;

public class KillCountStep implements TutorialStepStrategy{

    int amountKills;
    int maxTime;
    float timeElapsed = 0;
    String message;

    public KillCountStep(int amountKills,int maxTime,String message) {
        this.message = message;
        this.amountKills = amountKills;
        this.maxTime = maxTime;
    }

    @Override
    public void onEnter() {
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
       timeElapsed += deltaTime;
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return context.amountBallsKilled >= amountKills || timeElapsed > maxTime;
    }

    @Override
    public String getMessage() {
        return message;
    }
}