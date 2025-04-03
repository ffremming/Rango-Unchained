package io.github.RangoUnchained.Model.Tutorial;

import java.util.List;

/**
 * Manages the sequence of tutorial steps.
 * Uses a list of TutorialStepStrategy objects to delegate the flow of the tutorial.
 */
public class TutorialManager {
    private final List<TutorialStepStrategy> steps;
    private int currentStepIndex = 0;
    private TutorialStepStrategyContext context = new TutorialStepStrategyContext();

    /**
     * Constructs a TutorialManager with the given tutorial steps.
     *
     * @param steps the list of tutorial step strategies
     */
    public TutorialManager(List<TutorialStepStrategy> steps) {
        this.steps = steps;
    }

    /** starts the first (only if has been updated by an entity with the right component) */
    private void tryIntit(){
        if (!steps.isEmpty()) {
            if (currentStepIndex == 0)
            if (!((MoveLeftStrategy) steps.get(0)).started){
                steps.get(0).onEnter();
            }
        }
    }

    /**
     * Updates the current tutorial step.
     *
     * @param context   the context containing relevant game state
     * @param deltaTime the time elapsed since the last update
     */
    public void update(float deltaTime) {

        tryIntit();

        if (currentStepIndex < steps.size()) {
            TutorialStepStrategy currentStep = steps.get(currentStepIndex);
            currentStep.update(context, deltaTime);
            if (currentStep.isComplete(context)) {
                currentStepIndex++;
                if (currentStepIndex < steps.size()) {
                    steps.get(currentStepIndex).onEnter();
                }
                context = new TutorialStepStrategyContext();
            }
        }
    }

    /**
     * Returns the message for the current tutorial step.
     *
     * @return the current tutorial message, or a completion message if all steps are done
     */
    public String getCurrentMessage() {
        if (currentStepIndex < steps.size()) {
            return steps.get(currentStepIndex).getMessage();
        }
        return "Tutorial complete!";
    }

    public TutorialStepStrategyContext getCurrentContext(){
        return context;
    }
}
