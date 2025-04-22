package io.github.RangoUnchained.Model.ContactStrategies;

import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BallComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;
import io.github.RangoUnchained.Model.Systems.TutorialSystem;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.Dimension;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.TypeInfo;

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

        BallEntity ball;

        if (collisionEvent.entityA instanceof BallEntity) {
            ball = (BallEntity) collisionEvent.entityA;
        } else {
            ball = (BallEntity) collisionEvent.entityB;
        }

        SpriteComponent spriteComponent = (SpriteComponent) ball.getComponent(SpriteComponent.class);
        StatComponent statComponent = (StatComponent) ball.getComponent(StatComponent.class);

        float xPos = spriteComponent.getSprite().getX();
        float yPos = spriteComponent.getSprite().getY()+50;
        int timesPopped = statComponent.getTimesPopped();

        LevelController.getInstance().handleRemovalRequests(ball);
        LevelController.getInstance().getSystem(TutorialSystem.class).flagBallKilled();
        BallComponent ballcomp = (BallComponent) ball.getComponent(BallComponent.class);

        String spawnName = "Ball " + ballcomp.getTypeName();
       

        int size = timesPopped == 0 ? 2:timesPopped == 1 ? 1:0;
        Vector2 newVelocity = new Vector2((0), (5));


        if (size == 0){
            return;
        }

        EntityData dataRight = new EntityData();
        dataRight.dimension = new Dimension();
        dataRight.dimension.x = xPos +50;
        dataRight.dimension.y = yPos;
        dataRight.typeInfo = new TypeInfo();
        dataRight.typeInfo.type = "ball";
        dataRight.typeInfo.subType = ballcomp.getTypeName();
        dataRight.typeInfo.size = size;
        newVelocity.x = 2;
        dataRight.velocity = newVelocity;
        dataRight.name = spawnName;

        
        EntityData dataLeft = new EntityData();
        dataLeft.dimension = new Dimension();
        dataLeft.dimension.x = xPos -20;
        dataLeft.dimension.y = yPos;
        dataLeft.typeInfo = new TypeInfo();
        dataLeft.typeInfo.type = "ball";
        dataLeft.typeInfo.subType = ballcomp.getTypeName();
        dataLeft.typeInfo.size = size;
        newVelocity.x = -2;
        dataLeft.velocity = newVelocity;
        dataLeft.name = spawnName;

        LevelController.getInstance().handleSpawnRequests(dataLeft);
        LevelController.getInstance().handleSpawnRequests(dataRight);
    
    }
}