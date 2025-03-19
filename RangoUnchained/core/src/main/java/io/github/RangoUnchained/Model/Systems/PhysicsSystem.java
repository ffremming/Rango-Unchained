package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class PhysicsSystem implements ContactListener, Systems {

    private List<Entity> entities = new ArrayList<>();
    private World world;
//    private SimpleBodyFactory simpleBodyFactory;

    public PhysicsSystem(World world) {
//        world = new World(new Vector2(0, gravity*1000), true);
        world.setContactListener(this);
//        simpleBodyFactory = new SimpleBodyFactory(world);
        createPerimeters();
    }

//    public void addEntity(Entity entity) {
//        BodyComponent bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
//        if (bodyComponent == null) {
//            return;
//        }
//        bodyComponent.setBody(simpleBodyFactory.createBody(entity));
//    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public World getWorld() {
        return world;
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

    public void createPerimeters() {

        
        /* vi skulle legge til texture så vi kan se den.
        Problem: vi burde da lage entiteter som har body- og spritecomponent
        Problem: Hvis vi skal lage bodies må den vite om verdenen,
        derfor bruker vi simplebodyfactory, fordi det blir mer coupling/dependencies
        om vi gjør entity factory avhengig av et world objekt. Bodies burde altså lages
        et eget sted (simplebodyfactory) som hører til et physicssystem, fordi den uansett
        trenger bodies til å interacte med.
        */


        /* BodyDef leftWallBodyDef = new BodyDef();
        leftWallBodyDef.type = BodyDef.BodyType.StaticBody;
        leftWallBodyDef.position.set(500, 50);
        PolygonShape leftWallBox = new PolygonShape();
        leftWallBox.setAsBox(1000, 40);
        Body leftWallBody = world.createBody(leftWallBodyDef);
        leftWallBody.createFixture(leftWallBox, 0f);

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(500, 50);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(1000, 40);
        Body groundBody = world.createBody(groundBodyDef);
        groundBody.createFixture(groundBox, 0f);


        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(500, 50);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(1000, 40);
        Body groundBody = world.createBody(groundBodyDef);
        groundBody.createFixture(groundBox, 0f);
 */
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

    @Override
    public void clearSystems() {
        entities.clear();
        world.dispose();
    }
}
