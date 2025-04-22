package io.github.RangoUnchained.Model.Components;

/**
 * Component representing an entity's movement speed.
 */
public class SpeedComponent implements Component {
    private float currentSpeed;
    private final float baseSpeed;

    /**
     * Constructs a new {@link SpeedComponent} with the given base speed.
     * The current speed is initialized to the same value.
     *
     * @param baseSpeed the initial and base speed value
     */
    public SpeedComponent(float baseSpeed) {
        this.baseSpeed = baseSpeed;
        this.currentSpeed = baseSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }
}
