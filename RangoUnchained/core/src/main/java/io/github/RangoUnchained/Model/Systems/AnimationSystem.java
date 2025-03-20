package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class AnimationSystem implements Systems{

    public void animateSprite(Entity entity, String revertSpritePath, String spritePath, float delay){
        Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();
        sprite.setTexture(new Texture(spritePath));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
               sprite.setTexture(new Texture(revertSpritePath));
            }
        }, delay);
    }

    @Override
    public void clearSystems() {

    }
}
