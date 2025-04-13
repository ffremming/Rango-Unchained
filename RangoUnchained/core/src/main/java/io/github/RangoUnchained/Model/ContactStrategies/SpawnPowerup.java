package io.github.RangoUnchained.Model.ContactStrategies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;

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


        if (Math.random() < 0.2) { // 20% chance to spawn a powerup
            String[] powerupTypes = {"ShieldPowerUp", "SpeedPowerUp", "HealthUpPowerUp"};
            String powerupName = powerupTypes[(int) (Math.random() * powerupTypes.length)];

            float powerupX = spriteComponent.getSprite().getX() + spriteComponent.getSprite().getWidth() / 2;
            float powerupY = spriteComponent.getSprite().getY() + spriteComponent.getSprite().getHeight() / 2;
            float randomVelocityX = (float) (Math.random() * 8 - 4); // Random X velocity between -4 and 4
            float randomVelocityY = (float) (Math.random() * 3 + 3); // Random Y velocity between 3 and 8

            LevelController.getInstance().handleSpawnRequests(
            powerupX, powerupY + 50, 64, 64, powerupName, new Vector2(randomVelocityX, randomVelocityY)
            );
        }
    }
}
