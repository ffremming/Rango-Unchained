package io.github.RangoUnchained.Model.Components;

public class SpeedComponent implements Component {
    public float baseSpeed;
    public float currentSpeed;
    public float speedBoostTimer = 0;

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

    public float getSpeedBoostTimer() {
        return speedBoostTimer;
    }

    public void setSpeedBoostTimer(float speedBoostTimer) {
        this.speedBoostTimer = speedBoostTimer;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }
}
