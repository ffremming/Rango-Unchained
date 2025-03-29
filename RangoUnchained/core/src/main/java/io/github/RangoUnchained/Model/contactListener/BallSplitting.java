package io.github.RangoUnchained.Model.contactListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;

public class BallSplitting implements ContactStrategy{

    @Override
    public void setContactStrategies() {
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);
        centralContactListener.subscribe(
        BallEntity.class, ProjectileEntity.class,
        this::ProjectileBallContact, // For beginContact
        null);
    }

    public void ProjectileBallContact(CollisionEvent collisionEvent){

        Gdx.app.log("Projectile","fjfj");

        BallEntity ball;

        if (collisionEvent.entityA instanceof BallEntity) {
            ball = (BallEntity) collisionEvent.entityA;;
        } else {
            ball = (BallEntity) collisionEvent.entityB;
        }


        SpriteComponent spriteComponent = (SpriteComponent) ball.getComponent(SpriteComponent.class);
        StatComponent statComponent = (StatComponent) ball.getComponent(StatComponent.class);

        float xPos = spriteComponent.getSprite().getX();
        float yPos = spriteComponent.getSprite().getY()+50;
        int timesPopped = statComponent.getTimesPopped();
       
        Vector2 newVelocity = new Vector2((0), (5));

        LevelController.getInstance().handleRemovalRequests(ball);

        if (timesPopped == 0) {
            newVelocity.x = -3;

            LevelController.getInstance().handleSpawnRequests(xPos+50, yPos, 10, 10,
                "BallMedium", newVelocity);
            LevelController.getInstance().handleSpawnRequests(xPos-20, yPos, 10, 10,
            "BallMedium", newVelocity);

        } else if (timesPopped == 1) {
            newVelocity.x = 3;
            LevelController.getInstance().handleSpawnRequests(xPos+50, yPos, 5, 5,
                "BallSmall", newVelocity);
                LevelController.getInstance().handleSpawnRequests(xPos-20, yPos, 5, 5,
                "BallSmall", newVelocity);
        }
    }
    
}
