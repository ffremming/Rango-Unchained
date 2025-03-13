package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {

    private Sprite sprite;

    public SpriteComponent(String path) {
        sprite = new Sprite(new Texture(Gdx.files.internal(path)));
    }

    public Sprite getSprite() {
        return sprite;
    }
}
