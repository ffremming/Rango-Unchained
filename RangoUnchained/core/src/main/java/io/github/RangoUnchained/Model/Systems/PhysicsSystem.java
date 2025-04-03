package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.BounceComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.ContactStrategies.*;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.FloorEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;
import io.github.RangoUnchained.Views.Utils.Constants;

public class PhysicsSystem implements System, ContactStrategy {

    private World world;
    private ComponentFilter filter = new ComponentFilter();

    public PhysicsSystem() {
        world = new World(new Vector2(0, -10), true);

        filter
        .require(BodyComponent.class)
        .require(SpriteComponent.class);
       
    }


    public void setContactStrategies(){
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);
        centralContactListener.subscribe(
        BallEntity.class, FloorEntity.class,
        null, // For beginContact
        this::handleBallFloorCollision);
    }

        // Define a conversion factor (pixels per meter) – adjust as needed

    @Override
    public void updateEntity(Entity entity) {
        Sprite sprite = ((SpriteComponent) entity.getComponent(SpriteComponent.class)).getSprite();
        Body body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();

        // Convert physics (meters) position to screen (pixels) position
        float screenX = ((body.getPosition().x * Constants.PPM) - sprite.getWidth() / 2f);
        float screenY = ((body.getPosition().y * Constants.PPM) - sprite.getHeight() / 2f);
        
        //Gdx.app.log("spritePos",screenX+","+screenY);
        //Gdx.app.log("pos", entity.getClass().getName()+body.getPosition().y+","+body.getPosition().x);

        sprite.setPosition(screenX, screenY);
    }
  
                    
                
    private void handleBallFloorCollision(CollisionEvent collisionEvent) {
        Gdx.app.log("contact","ballfloor");
        BallEntity ball;

        if (collisionEvent.entityA instanceof BallEntity) {
            ball = (BallEntity) collisionEvent.entityA;;
        } else {
            ball = (BallEntity) collisionEvent.entityB;
        }

        BodyComponent ballBodyComponent = (BodyComponent) ball.getComponent(BodyComponent.class);
        Body ballBody = ballBodyComponent.getBody();

        Vector2 currentVelocity = ballBody.getLinearVelocity();

        final float  STANDARDYVELOCITY = (float)((BounceComponent) ball.getComponent(BounceComponent.class)).type;
        final float  STANDARDXVELOCITY = 5;

        final float YTRESHOLD = -10;
        final float MAXIMUMXTRESHOLD = 5;
        final float MINIMUMXTRESHOLD = 1;

        if (currentVelocity.y > YTRESHOLD) {
            ballBody.setLinearVelocity(new Vector2(currentVelocity.x,STANDARDYVELOCITY));
        }
        
        currentVelocity = ballBody.getLinearVelocity();

        //if ball bounces too much to the sides
        if (Math.abs(currentVelocity.x) > MAXIMUMXTRESHOLD) {
            // if to the left
            if (currentVelocity.x<0){
                ballBody.setLinearVelocity(new Vector2(-STANDARDXVELOCITY,currentVelocity.y));

            // if to the right
            } else {
                ballBody.setLinearVelocity(new Vector2(STANDARDXVELOCITY,currentVelocity.y));

            }
        } else if (Math.abs(currentVelocity.x) < MINIMUMXTRESHOLD){
            ballBody.setLinearVelocity(new Vector2(STANDARDXVELOCITY/2,currentVelocity.y));

        }


    }      

    public World getWorld() {
        return world;
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
