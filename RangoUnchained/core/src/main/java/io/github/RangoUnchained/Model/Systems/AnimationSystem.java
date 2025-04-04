package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;

import io.github.RangoUnchained.Model.Components.AnimationComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class AnimationSystem implements System{

    private ComponentFilter filter = new ComponentFilter();

    public AnimationSystem() {
        filter.require(InputComponent.class);
        filter.require(AnimationComponent.class);
    }



    public void updateEntity(Entity entity, float delta) {

        // Retrieve components
        InputComponent input = (InputComponent) entity.getComponent(InputComponent.class);
        SpriteComponent spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class);
        AnimationComponent animationComponent = (AnimationComponent) entity.getComponent(AnimationComponent.class);

        Sprite sprite = spriteComponent.getSprite();

        // Check input and update animation state
        if (input.isLeft()) {
            animationComponent.setPlayerState("LEFT");
            Gdx.app.log("Animation", "Left");
        }
        else if (input.isRight()) {
            animationComponent.setPlayerState("RIGHT");
            Gdx.app.log("Animation", "right");

        }
        else if (input.isShoot()) {
            animationComponent.setPlayerState("SHOOTING");
            Gdx.app.log("Animation", "shoot");

        }  else {
            animationComponent.setPlayerState("IDLE");
            Gdx.app.log("Animation", "idle");

        }


        /*switch (input.getInputState()) {
            case LEFT:
                animationComponent.setPlayerState("LEFT");
                break;
            case RIGHT:
                animationComponent.setPlayerState("RIGHT");
                break;
            case SHOOTING:
                animationComponent.setPlayerState("SHOOTING");
                break;
            case IDLE:
                animationComponent.setPlayerState("IDLE");
                break;
        }*/

        Gdx.app.log("Animation", animationComponent.getPlayerState().toString());
        // Update animation time
        animationComponent.increaseDelta(delta);

        Animation<TextureRegion> currentAnimation = animationComponent.getAnimation(animationComponent.getPlayerState());

        // Get current frame
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationComponent.getFrame(), true);

        // Update sprite with new frame
        sprite.setRegion(currentFrame);
    }
   /* @Override
    public void updateEntity(Entity entity, float delta) {
        InputComponent input = (InputComponent) entity.getComponent(InputComponent.class);
        Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();
        AnimationComponent animationComponent = ((AnimationComponent) entity.getComponent(AnimationComponent.class));

        if (input.isLeft()) {
            animationComponent.setPlayerState("LEFT");
        }
        animationComponent.increaseDelta(delta);

        Animation<TextureRegion> currentAnimation = animationComponent.getAnimation(animationComponent.getPlayerState());

        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationComponent.getFrame(), true);

        sprite.setRegion(currentFrame);

       *//* if(input.isShoot()){
            animateSprite(sprite, "Rango/Rango.png", "Rango/Rango-shoot.png", 1);
        }
        if(input.isLeft()){
            animateSprite(sprite, "Rango/Rango.png", "Rango/Rango-left.png", 0);
        }
        if(input.isRight()){
            animateSprite(sprite, "Rango/Rango.png", "Rango/Rango-right.png", 0);
        }*//*
    }*/

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
