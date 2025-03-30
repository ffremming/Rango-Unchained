package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public class PowerUpComponent implements Component {

    private int powerUpType;
    private float powerUpTimer;

    /*public PowerUpComponent(String path, int powerUpType) {
        texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        this.powerUpType = powerUpType;
        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight());

    }*/

    public PowerUpComponent(int powerUpType) {
        this.powerUpType = powerUpType;
        this.powerUpTimer = 0;
    }

    public int getPowerUpType() {
        return powerUpType;
    }
}
