package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public class PowerUpComponent implements Component {

    private Sprite sprite;
    private Texture texture;
    private int powerUpType;

    public PowerUpComponent(String path, int powerUpType) {
        texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        this.powerUpType = powerUpType;
        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight());

    }

    public PowerUpComponent(BodyComponent bodyComponent, SpriteComponent spriteComponent,
                            String path, float width, float height, int powerUpType) {
        texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        this.powerUpType = powerUpType;
        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setSize(width, height);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }

    public int getPowerUpType() {
        return powerUpType;
    }
}
