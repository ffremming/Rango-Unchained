package io.github.RangoUnchained.Model.Tutorial;

import io.github.RangoUnchained.Controllers.LevelController;

public class LevelCountDown implements TutorialStepStrategy{

    float counter = 0;
    
    
    @Override
    public void onEnter() {
       
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        counter += deltaTime;
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        if ( counter >3){
            LevelController.getInstance().initializeSystems(1);
        }
        return counter >3;
    }

    @Override
    public String getMessage() {
        return String.format("Tutorial completed, Level 1 is starting in %.0f seconds", counter);
    }
    
}
