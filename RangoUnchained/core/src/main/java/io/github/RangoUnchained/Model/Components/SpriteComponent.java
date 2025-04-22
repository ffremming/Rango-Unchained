package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Component representing a sprite for an entity.
 */
public class SpriteComponent implements Component {

    private final Sprite sprite;
    private Texture texture;
    private final String path;

    /**
     * Constructs a {@link SpriteComponent} from an image path.
     *
     * @param path path to the texture
     */
    public SpriteComponent(String path) {
        texture = new Texture(Gdx.files.internal(path));

        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight());
        this.path = path;
    }

    /**
     * Constructs a {@link SpriteComponent} from an image path with a specified size.
     *
     * @param path   path to the texture
     * @param width  width of the sprite
     * @param height height of the sprite
     */
    public SpriteComponent(String path, float width, float height) {
        this.path = path;
        System.out.println(path);

        texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        sprite = new Sprite(texture);
        sprite.setColor(Color.WHITE);
        sprite.setSize(width, height);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }

    /**
     * Constructs a {@link SpriteComponent} from a texture region.
     *
     * @param region texture region to use
     * @param width  width of the sprite
     * @param height height of the sprite
     * @param path   original asset path
     */
    public SpriteComponent(TextureRegion region, float width, float height, String path) {

        sprite = new Sprite(region);
        sprite.setColor(Color.WHITE);
        sprite.setSize(width, height);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.path = path;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public String getPath() {
        return path;
    }

}
