package io.github.RangoUnchained.Model.Tutorial;

/**
 * Interface for a tutorial step using the Strategy Pattern.
 * Each tutorial step implements its own logic to update and check if it is complete.
 */
public interface TutorialStepStrategy {
    /**
     * Called once when this tutorial step becomes active.
     */
    void onEnter();
    
    /**
     * Updates this tutorial step with the given context.
     *
     * @param context   the context carrying relevant game state (e.g., input)
     * @param deltaTime the time elapsed since the last update, in seconds
     */
    void update(TutorialStepStrategyContext context, float deltaTime);
    
    /**
     * Checks if the tutorial step is complete based on the provided context.
     *
     * @param context the context carrying relevant game state (e.g., input)
     * @return true if the step is complete, false otherwise
     */
    boolean isComplete(TutorialStepStrategyContext context);
    
    /**
     * Returns the message to be displayed for this tutorial step.
     *
     * @return the tutorial message
     */
    String getMessage();
}
