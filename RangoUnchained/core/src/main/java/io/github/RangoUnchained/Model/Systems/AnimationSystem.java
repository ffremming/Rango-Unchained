package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.RangoUnchained.Model.Components.AnimationComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class AnimationSystem implements Systems {

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

        // Update animation time
        animationComponent.increaseDelta(delta);

        Animation<TextureRegion> currentAnimation = animationComponent.getAnimation(animationComponent.getPlayerState());

        // Get current frame
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationComponent.getFrame(), true);
        // Update sprite with new frame
        sprite.setRegion(currentFrame);
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }


}
