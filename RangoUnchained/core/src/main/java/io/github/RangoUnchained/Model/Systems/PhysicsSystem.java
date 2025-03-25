package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class PhysicsSystem implements ContactListener, System {

    private World world;

    public PhysicsSystem() {
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(this);

        filter
        .require(BodyComponent.class)
        .require(SpriteComponent.class);
    }

    @Override
        public void updateEntity(Entity entity) {
            Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();
            Body body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();

            sprite.setPosition(
                body.getPosition().x - sprite.getWidth()/2,
                body.getPosition().y - sprite.getHeight()/2
            );
        }


    private void handleBallPlayerCollision(BallEntity ball, PlayerEntity player) {

        SpriteComponent spriteComponent = (SpriteComponent) ball.getComponent(SpriteComponent.class);
        StatComponent statComponent = (StatComponent) ball.getComponent(StatComponent.class);
        BodyComponent bodyComponent = (BodyComponent) ball.getComponent(BodyComponent.class);

        float xPos = spriteComponent.getSprite().getX();
        float yPos = spriteComponent.getSprite().getY();
        int timesPopped = statComponent.getTimesPopped();
        Vector2 oldVelocity = bodyComponent.getBody().getLinearVelocity();
        Vector2 newVelocity = new Vector2(-(oldVelocity.x), -(oldVelocity.y));

        LevelController.getInstance().handleRemovalRequests(ball);

        if (timesPopped == 0) {
            LevelController.getInstance().handleSpawnRequests(xPos, yPos, 10, 10,
                "BallMedium", newVelocity, 2);

        } else if (timesPopped == 1) {
            LevelController.getInstance().handleSpawnRequests(xPos, yPos, 5, 5,
                "BallSmall", newVelocity, 2);
        }
    }


    @Override
    public void beginContact(Contact contact) {
        // Retrieve colliding fixtures
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        if (dataA == null || dataB == null) return;
        if (!(dataA instanceof Entity) || !(dataB instanceof Entity)) return;

        Entity entityA = (Entity) dataA;
        Entity entityB = (Entity) dataB;

        // Check ball-player collision
        if (entityA instanceof BallEntity && entityB instanceof PlayerEntity) {
            handleBallPlayerCollision((BallEntity) entityA, (PlayerEntity) entityB);
        } else if (entityA instanceof PlayerEntity && entityB instanceof BallEntity) {
            handleBallPlayerCollision((BallEntity) entityB, (PlayerEntity) entityA);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public World getWorld() {
        return world;
    }
}
