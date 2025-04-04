package io.github.RangoUnchained.Model.Tutorial;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.GamePlayView;

public class LevelCountDown implements TutorialStepStrategy{

    float counter = 0;
    
    
    @Override
    public void onEnter() {
        GameFileHandler.getInstance();
        GameFileHandler.setProgress(LevelController.getInstance().getLevel().levelNumber+1);
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        counter += deltaTime;
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        if ( counter >3){
            LevelController.getInstance().completed = true;
        }
        return counter >3;
    }

    @Override
    public String getMessage() {
        return String.format("Tutorial completed, Level 1 is starting in %.0f seconds", counter);
    }
    
}
