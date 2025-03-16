package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class PhysicsSystem implements ContactListener {

    private List<Entity> entities = new ArrayList<>();
    private World world;
    private SimpleBodyFactory simpleBodyFactory;

    public PhysicsSystem(float gravity) {
        world = new World(new Vector2(0, gravity), true);
        world.setContactListener(this);
        simpleBodyFactory = new SimpleBodyFactory(world);
    }

    public void addEntity(Entity entity) {
        BodyComponent bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        if (bodyComponent == null) {
            return;
        }
        bodyComponent.setBody(simpleBodyFactory.createBody(entity));
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }


    @Override
    public void beginContact(Contact contact) {

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
}
