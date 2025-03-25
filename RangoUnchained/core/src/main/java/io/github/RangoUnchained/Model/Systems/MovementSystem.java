package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Views.Utils.Constants;

public class MovementSystem implements System {

    private ComponentFilter filter = new ComponentFilter();

    public MovementSystem() {
        filter
        .require(BodyComponent.class)
        .require(InputComponent.class)
        .require(SpriteComponent.class);
    }


    // Updates every playable entity's position based on input and velocity
    // Method called from controllers for updates
    public void updateEntity(Entity entity) {

        BodyComponent bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        InputComponent inputComponent = (InputComponent) entity.getComponent(InputComponent.class);
        SpriteComponent spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class);
        Body body = bodyComponent.getBody();

        if (inputComponent.isShoot()) {
            bodyComponent.getBody().setLinearVelocity(0, 0);
            //spriteComponent.getSprite().setPosition(Constants.metersToPixels(body.getPosition().x), Constants.metersToPixels(body.getPosition().y));
            java.lang.System.out.println();

            inputComponent.setShoot(false);
        } else if (inputComponent.isLeft()) {
            bodyComponent.getBody().setLinearVelocity(-500, 0);
            //spriteComponent.getSprite().setPosition(Constants.metersToPixels(body.getPosition().x), Constants.metersToPixels(body.getPosition().y));
        } else if (inputComponent.isRight()){
            bodyComponent.getBody().setLinearVelocity(500, 0);
            //spriteComponent.getSprite().setPosition(Constants.metersToPixels(body.getPosition().x), Constants.metersToPixels(body.getPosition().y));
        } else {
            bodyComponent.getBody().setLinearVelocity(0, 0);
            //spriteComponent.getSprite().setPosition(Constants.metersToPixels(body.getPosition().x), Constants.metersToPixels(body.getPosition().y));
        }
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
