package io.github.RangoUnchained.Model.Tutorial;

import io.github.RangoUnchained.Model.Components.InputComponent;

/**
 * Context for a tutorial step strategy, holding game state needed for evaluation.
 * Currently includes the input component; extend this class with additional fields if necessary.
 */
public class TutorialStepStrategyContext {
    /**
     * The input component containing the current player inputs.
     */

    
    public boolean left;
    public boolean right;
    public boolean shoot;
    public boolean hit;
    public boolean killed;
    public boolean splitted;
    public boolean powerup;
    public boolean takenDamage;
    public int amountBallsKilled;
    public boolean pickedUpPowerup;
}
