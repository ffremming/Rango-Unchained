package io.github.RangoUnchained.Model.Tutorial;
/**
 * Tutorial step that instructs the player to move left.
 */
public class ShootStrategy implements TutorialStepStrategy {

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
        return context.shoot;
    }

    @Override
    public String getMessage() {
        return "press the button in the bottom right to shoot";
    }
}
