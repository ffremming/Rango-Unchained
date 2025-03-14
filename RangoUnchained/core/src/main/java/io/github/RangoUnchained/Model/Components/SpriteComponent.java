package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.graphics.Texture;

public class SpriteComponent implements Component {

    private Texture texture;

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
