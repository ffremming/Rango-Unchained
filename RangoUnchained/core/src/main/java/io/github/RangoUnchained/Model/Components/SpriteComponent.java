package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent implements Component {

    private Sprite sprite;
    private Texture texture;
    private String path;

    public SpriteComponent(String path) {
        texture = new Texture(Gdx.files.internal(path));



        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight());
    }

    public SpriteComponent(String path, float width, float height) {
        this.path = path;

        texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setSize(width, height);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }

    public SpriteComponent(TextureRegion region, float width, float height) {

        sprite = new Sprite(region);
        sprite.setColor(Color.WHITE);
        sprite.setSize(width, height);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }

    public Sprite getSprite(float degree) {
        return sprite;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getPath() {
        return path;
    }

}
