package io.github.RangoUnchained.Model.Tutorial;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;

public class KillSpawnStrategy implements TutorialStepStrategy{

    @Override
    public void onEnter() {
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
       
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return context.amountBallsKilled == 2;
    }

    @Override
    public String getMessage() {
        return "when a large enemy is killed, it splits in two";
    }
}