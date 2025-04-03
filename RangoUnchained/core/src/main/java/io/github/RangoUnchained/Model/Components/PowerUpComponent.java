package io.github.RangoUnchained.Model.Components;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public class PowerUpComponent implements Component {

    public static final int SPEED = 0;
    public static  final int SHIELD = 1;
    public static final int BALLSIZE = 2;
    public static final int BALLBOUNCE = 3;


    private int powerUpType;
    private float powerUpTimer;
    private Map<Integer, Float> activePowerUps; // Stores power-up type and remaining duration

    public PowerUpComponent(int powerUpType) {
        this.powerUpType = powerUpType;
        this.powerUpTimer = 0;
        this.activePowerUps = new HashMap<>();
    }

    public PowerUpComponent() {
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
