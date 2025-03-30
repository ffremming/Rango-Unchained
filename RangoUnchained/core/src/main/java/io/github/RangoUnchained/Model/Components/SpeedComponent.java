package io.github.RangoUnchained.Model.Components;

public class SpeedComponent implements Component {
    public float baseSpeed;
    public float currentSpeed;

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
}
