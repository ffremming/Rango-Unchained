package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class SimpleBodyFactory {

    World world;

    public SimpleBodyFactory(World world) {
        this.world = world;
    }

    public Body createBody(Entity entity) {

        SpriteComponent spriteComponent = (SpriteComponent) entity.getComponent(SpriteComponent.class);
        BodyComponent bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        FixtureDef fixtureDef = new FixtureDef();
        Body body = world.createBody(bodyComponent.getBodyDef()); // Create body and attach to world from PhysicsSystem
        body.setUserData(entity); // Attach entity to the body

        // If statements to edit the values of fixture.
        if (entity instanceof PlayerEntity) {
            // Logic for playerentities
            fixtureDef.shape = new PolygonShape();
            fixtureDef.friction = 0.4f;
        }

        if (entity instanceof BallEntity) {
            // Logic for ballentities
            CircleShape circle = new CircleShape();
            fixtureDef.shape = circle;
            fixtureDef.density = 0.5f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 1f; // Make it bounce the same height every time

            StatComponent statComponent = (StatComponent) entity.getComponent(StatComponent.class);
            if (statComponent.getTimesPopped() == 0) {
                circle.setRadius(10);
                body.setLinearVelocity(10, 2);
            }
            if (statComponent.getTimesPopped() == 1) {
                circle.setRadius(6);
                body.setLinearVelocity(6, 2);
            }
            if (statComponent.getTimesPopped() == 2) {
                circle.setRadius(3);
                body.setLinearVelocity(3, 2);
            }
        }

        // Set the fixture of the body corresponding to the fixturedef of the correct entity
        Fixture fixture = body.createFixture(fixtureDef);
        return body;
    }

}
