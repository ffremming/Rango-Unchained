package io.github.RangoUnchained.Model.Components;

import java.util.HashMap;
import java.util.Map;

/**
 * Component representing power-up state for an entity.
 */
public class PowerUpComponent implements Component {

    public static final int SPEED = 0;
    public static  final int SHIELD = 1;
    public static  final int HEALTH = 2;

    private final int powerUpType;
    private final Map<Integer, Float> activePowerUps; // Stores power-up type and remaining duration

    /**
     * Constructs a {@link PowerUpComponent} with the given power-up type.
     *
     * @param powerUpType the type of power-up applied to the entity
     */
    public PowerUpComponent(int powerUpType) {
        this.powerUpType = powerUpType;
        this.activePowerUps = new HashMap<>();
    }

    public int getPowerUpType() {
        return powerUpType;
    }

    public void addPowerUp(int powerUpType, float duration) {
        activePowerUps.put(powerUpType, duration);
    }

    public Map<Integer, Float> getActivePowerUps() {
        return activePowerUps;
    }
}
