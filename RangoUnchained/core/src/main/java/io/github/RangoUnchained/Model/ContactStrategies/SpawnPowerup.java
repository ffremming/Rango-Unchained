package io.github.RangoUnchained.Model.ContactStrategies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.Dimension;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.TypeInfo;

public class SpawnPowerup implements ContactStrategy{

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


        if (Math.random() < 0.9) { // 20% chance to spawn a powerup
            String[] powerupTypes = {"Shield", "Speed", "Health"};
            String powerupName = powerupTypes[(int) (Math.random() * powerupTypes.length)];

            float powerupX = spriteComponent.getSprite().getX() + spriteComponent.getSprite().getWidth() / 2;
            float powerupY = spriteComponent.getSprite().getY() + spriteComponent.getSprite().getHeight() / 2;
            float randomVelocityX = (float) (Math.random() * 8 - 4); // Random X velocity between -4 and 4
            float randomVelocityY = (float) (Math.random() * 3 + 3); // Random Y velocity between 3 and 8

           Vector2 velocity = new Vector2(randomVelocityX, randomVelocityY);

            EntityData data = new EntityData();
            data.velocity = velocity;
            data.typeInfo = new TypeInfo();
            data.typeInfo.type = "Powerup";
            data.typeInfo.subType = powerupName;
            data.dimension = new Dimension();
            data.dimension.x = powerupX;
            data.dimension.y = powerupY +50;
            data.name = "Powerup";

            LevelController.getInstance().handleSpawnRequests(data);

        }
    }
}
