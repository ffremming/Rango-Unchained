package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.PhysicsComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Views.Utils.Constants;

public class PhysicsSystem implements ContactListener, System {

    private World world;
    private ComponentFilter filter = new ComponentFilter();

    public PhysicsSystem() {
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(this);
        
        filter
        .require(BodyComponent.class)
        .require(SpriteComponent.class)
        .require(PhysicsComponent.class);
    }

        // Define a conversion factor (pixels per meter) – adjust as needed

    @Override
    public void updateEntity(Entity entity) {
        Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();
        Body body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();
        PhysicsComponent physComp = ((PhysicsComponent)entity.getComponent(PhysicsComponent.class));

        // Convert physics (meters) position to screen (pixels) position
        float screenX = ((body.getPosition().x * Constants.PPM) - sprite.getWidth() / 2f);
        float screenY = ((body.getPosition().y * Constants.PPM) - sprite.getHeight() / 2f);
        
        //Gdx.app.log("spritePos",screenX+","+screenY);
        //Gdx.app.log("pos", entity.getClass().getName()+body.getPosition().y+","+body.getPosition().x);

        sprite.setPosition(screenX, screenY);
        physComp.decrementContactLock();
    }


    private void handleProjectileBallCollision(BallEntity ball, ProjectileEntity player) {

        SpriteComponent spriteComponent = (SpriteComponent) ball.getComponent(SpriteComponent.class);
        StatComponent statComponent = (StatComponent) ball.getComponent(StatComponent.class);
        BodyComponent bodyComponent = (BodyComponent) ball.getComponent(BodyComponent.class);

        float xPos = spriteComponent.getSprite().getX();
        float yPos = spriteComponent.getSprite().getY()+50;
        int timesPopped = statComponent.getTimesPopped();
       
        Vector2 newVelocity = new Vector2((0), (5));

        LevelController.getInstance().handleRemovalRequests(ball);

        if (timesPopped == 0) {
            newVelocity.x = -5;

            LevelController.getInstance().handleSpawnRequests(xPos, yPos, 10, 10,
                "BallMedium", newVelocity);
            LevelController.getInstance().handleSpawnRequests(xPos, yPos, 10, 10,
            "BallMedium", newVelocity);

        } else if (timesPopped == 1) {
            newVelocity.x = 5;
            LevelController.getInstance().handleSpawnRequests(xPos, yPos, 5, 5,
                "BallSmall", newVelocity);
                LevelController.getInstance().handleSpawnRequests(xPos, yPos, 5, 5,
                "BallSmall", newVelocity);
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

        //check locks
        if (((PhysicsComponent)((Entity) dataA).getComponent(PhysicsComponent.class))
        .isContactLocked()){return;}
        if (((PhysicsComponent)((Entity) dataB).getComponent(PhysicsComponent.class))
        .isContactLocked()){return;}

        Entity entityA = (Entity) dataA;
        Entity entityB = (Entity) dataB;

        Gdx.app.log("collision","basic");


        // Check ball-player collision
        if (entityA instanceof BallEntity && entityB instanceof ProjectileEntity) {
            handleProjectileBallCollision((BallEntity) entityA, (ProjectileEntity) entityB);
        } else if (entityA instanceof ProjectileEntity && entityB instanceof BallEntity) {
            handleProjectileBallCollision((BallEntity) entityB, (ProjectileEntity) entityA);
        } else if (entityA instanceof ObstacleEntity && entityB instanceof BallEntity) {
            handleBallObstacleCollision(entityA, entityB);
        } else if (entityA instanceof BallEntity  && entityB instanceof ObstacleEntity) {
            handleBallObstacleCollision(entityA, entityB);
        }
    }
            
                    
                
    private void handleBallObstacleCollision(Entity entityA, Entity entityB) {
        BallEntity ball;
        ObstacleEntity obstacle;
        Gdx.app.log("collision","11");

        if (entityA instanceof BallEntity) {
            ball = (BallEntity) entityA;
            obstacle = (ObstacleEntity) entityB;
        } else {
            ball = (BallEntity) entityB;
            obstacle = (ObstacleEntity) entityA;
        }

        BodyComponent ballBodyComponent = (BodyComponent) ball.getComponent(BodyComponent.class);
        Body ballBody = ballBodyComponent.getBody();


        final float BOOST_AMOUNT = 200.0f;


        // Apply a small upward boost to the ball
        Vector2 currentVelocity = ballBody.getLinearVelocity();
        ballBody.applyLinearImpulse(new Vector2(0, BOOST_AMOUNT),
                                  ballBody.getWorldCenter(), true);
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

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
