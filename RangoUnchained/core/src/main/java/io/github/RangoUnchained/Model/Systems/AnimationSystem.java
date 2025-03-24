package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class AnimationSystem implements Systems{

    private List<Entity> entities = new ArrayList<>();

    public void designAnimation(){
        InputComponent p1_input = (InputComponent) entities.get(0).getComponent(InputComponent.class);
        Sprite p1_sprite = ((SpriteComponent) entities.get(0).getComponent(SpriteComponent.class)).getSprite();

        if(p1_input.isShoot()){
            animateSprite(p1_sprite, "Rango/Rango.png", "Rango/Rango-shoot.png", 1);
        }
        if(p1_input.isLeft()){
            animateSprite(p1_sprite, "Rango/Rango.png", "Rango/Rango-left.png", 0);
        }
        if(p1_input.isRight()){
            animateSprite(p1_sprite, "Rango/Rango.png", "Rango/Rango-right.png", 0);
        }
    }

    private void animateSprite(Sprite sprite, String revertSpritePath, String spritePath, float delay){
//        Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();
        sprite.setTexture(new Texture(spritePath));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
               sprite.setTexture(new Texture(revertSpritePath));
            }
        }, delay);
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }

    @Override
    public void clearSystems() {
        entities.clear();
    }
}
