package io.github.RangoUnchained.Model.Components;

/**
 * A minimal component that holds tutorial state.
 * This component is attached to an entity to signal that the tutorial is active and
 * to store the current message for display.
 */
public class TutorialComponent implements Component {
    /**
     * Flag indicating whether the tutorial is active.
     */
    public boolean active = true;
    
    /**
     * The current message to display on the UI.
     */
    public String message = "";
}
