package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
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
        .require(SpriteComponent.class)
        .require(SpeedComponent.class);
    }


    // Updates every playable entity's position based on input and velocity
    // Method called from controllers for updates
    @Override
    public void updateEntity(Entity entity) {
        BodyComponent bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        InputComponent inputComponent = (InputComponent) entity.getComponent(InputComponent.class);
        Body body = bodyComponent.getBody();
        SpeedComponent speedComponent = (SpeedComponent) entity.getComponent(SpeedComponent.class);

        float moveSpeed = speedComponent.baseSpeed;

        // Get the current velocity (we'll preserve the y-component, for example)
        Vector2 currentVelocity = body.getLinearVelocity();
        float newVelocityX = 0f;

        // Determine the new x-velocity based on input
        if (inputComponent.isLeft()) {
            newVelocityX = -moveSpeed;
        } else if (inputComponent.isRight()) {
            newVelocityX = moveSpeed;
        }

        // If you want to handle a shoot action or other input, you can process that separately
        if (inputComponent.isShoot()) {
            // Perform shoot logic here if needed.
            inputComponent.setShoot(false);
        }

        // Update the body's velocity
        body.setLinearVelocity(newVelocityX, currentVelocity.y);
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
