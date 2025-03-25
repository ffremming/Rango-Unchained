package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class AnimationSystem implements System{
    
    private ComponentFilter filter = new ComponentFilter();

    public AnimationSystem() {
        filter
        .require(InputComponent.class);
    }

    @Override
    public void updateEntity(Entity entity) {
        InputComponent input = (InputComponent) entity.getComponent(InputComponent.class);
        Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();

        if(input.isShoot()){
            animateSprite(sprite, "Rango/Rango.png", "Rango/Rango-shoot.png", 1);
        }
        if(input.isLeft()){
            animateSprite(sprite, "Rango/Rango.png", "Rango/Rango-left.png", 0);
        }
        if(input.isRight()){
            animateSprite(sprite, "Rango/Rango.png", "Rango/Rango-right.png", 0);
        }
    }

    private void animateSprite(Sprite sprite, String revertSpritePath, String spritePath, float delay){
        sprite.setTexture(new Texture(spritePath));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
               sprite.setTexture(new Texture(revertSpritePath));
            }
        }, delay);
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}