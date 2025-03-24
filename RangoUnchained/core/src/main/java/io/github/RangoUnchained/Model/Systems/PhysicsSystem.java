package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;

public class PhysicsSystem implements ContactListener, Systems {

    private List<Entity> entities = new ArrayList<>();
    private World world;
//    private SimpleBodyFactory simpleBodyFactory;
    private List<SpawnRequest> spawnRequests = new ArrayList<>();
    private List<Entity> removalQueue = new ArrayList<>();

    public PhysicsSystem(World world) {
        this.world = world;
        world.setContactListener(this);
    }

    public void updatePhysics() {
        for (Entity e : entities) {
            Sprite sprite = ((SpriteComponent) e.getComponent(SpriteComponent.class)).getSprite();
            Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();

            sprite.setPosition(
                body.getPosition().x - sprite.getWidth()/2,
                body.getPosition().y - sprite.getHeight()/2
            );

//            if (e instanceof BallEntity) {
//                StatComponent statComponent = (StatComponent) e.getComponent(StatComponent.class);
//                if (statComponent.getTimesPopped() >= 2) {
//                    return;
//                    //Dette er den minste ballen. ikke spawn ny ball
//                }
//
//                //Slett ball
//                System.out.println("COCKCKCKCKCKKKKKKKKKKKKKKKKKKK");
//                removeEntity(e);
//
//                //Lag 2 nye baller med halvparten radius timepsPopped ++;
//
//            }
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
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


    private void handleBallPlayerCollision(BallEntity ball, PlayerEntity player) {
        System.out.println("BallPlayerCollision");

        SpriteComponent spriteComponent = (SpriteComponent) ball.getComponent(SpriteComponent.class);
        StatComponent statComponent = (StatComponent) ball.getComponent(StatComponent.class);
        BodyComponent bodyComponent = (BodyComponent) ball.getComponent(BodyComponent.class);

        float xPos = spriteComponent.getSprite().getX();
        float yPos = spriteComponent.getSprite().getY();
        int timesPopped = statComponent.getTimesPopped();
        Vector2 oldVelocity = bodyComponent.getBody().getLinearVelocity();
        Vector2 newVelocity = new Vector2(-(oldVelocity.x), -(oldVelocity.y));

        removalQueue.add(ball);

        if (timesPopped == 0) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            spawnRequests.add(new SpawnRequest(xPos - 5f, yPos, 5f,
                "Balls/Medium ball.png", 1, newVelocity));
            spawnRequests.add(new SpawnRequest(xPos + 5f, yPos, 5f,
                "Balls/Medium ball.png", 1, newVelocity));
        } else if (timesPopped == 1) {
            spawnRequests.add(new SpawnRequest(xPos - 2.5f, yPos, 2.5f,
                "Balls/Small ball.png", 2, newVelocity));
            spawnRequests.add(new SpawnRequest(xPos + 2.5f, yPos, 2.5f,
                "Balls/Small ball.png", 2, newVelocity));
        }
    }


    public static class SpawnRequest {
        public float x, y, radius;
        public String spritePath;
        public int timesPopped;
        public Vector2 velocity;
        public SpawnRequest(float x, float y, float radius, String spritePath, int timesPopped,
                            Vector2 velocity) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.spritePath = spritePath;
            this.timesPopped = timesPopped;
            this.velocity = velocity;
        }
    }

    public World getWorld() {
        return world;
    }


    public List<SpawnRequest> getSpawnRequests() {
        return spawnRequests;
    }

    public List<Entity> getRemovalQueue() {
        return removalQueue;
    }

    public void clearRemovalQueue() {
        removalQueue.clear();
    }

    public void clearSpawnRequests() {
        spawnRequests.clear();
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

    @Override
    public void clearSystems() {
        entities.clear();
        world.dispose();
    }
}
