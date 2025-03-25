package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {

    private Sprite sprite;
    private Texture texture;

    public SpriteComponent(String path) {
        texture = new Texture(Gdx.files.internal(path));
        sprite = new Sprite(texture);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        
    }

    public SpriteComponent(String path, float width, float height) {
        texture = new Texture(Gdx.files.internal(path));
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

    }

    public Sprite getSprite() {
        return sprite;
    }
    public Texture getTexture() {
        return texture;
    }

}
