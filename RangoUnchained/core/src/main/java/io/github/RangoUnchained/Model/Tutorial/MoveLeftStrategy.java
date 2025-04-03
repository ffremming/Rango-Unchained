package io.github.RangoUnchained.Model.Tutorial;

/**
 * Tutorial step that instructs the player to move left.
 */
public class MoveLeftStrategy implements TutorialStepStrategy {

    boolean started = false;

    @Override
    public void onEnter() {
        started = true;
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        // No per-frame logic required for this simple step.
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return context.left;
    }

    @Override
    public String getMessage() {
        if (started){
            return "press A to move left";
        }
        else {return "";}
        
    }
}
