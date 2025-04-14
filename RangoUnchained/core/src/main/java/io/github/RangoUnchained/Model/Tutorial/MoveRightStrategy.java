package io.github.RangoUnchained.Model.Tutorial;

/**
 * Tutorial step that instructs the player to move left.
 */
public class MoveRightStrategy implements TutorialStepStrategy {

    @Override
    public void onEnter() {
        // Initialization logic when the step is activated.
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        // No per-frame logic required for this simple step.
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return context.right;
    }

    @Override
    public String getMessage() {
        return "use the joystick to move right";
    }
}
