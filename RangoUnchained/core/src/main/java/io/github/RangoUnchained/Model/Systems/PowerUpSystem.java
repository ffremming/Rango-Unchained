package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.PowerUpEntity;
import io.github.RangoUnchained.Model.PowerUps.PowerUpStrategy;
import io.github.RangoUnchained.Model.PowerUps.SpeedUpPowerUp;
import io.github.RangoUnchained.Model.contactListener.ContactStrategy;

public class PowerUpSystem implements System, ContactStrategy {
    private ComponentFilter filter = new ComponentFilter();
    private Map<Integer, PowerUpStrategy> powerUpStrategies = new HashMap<>();


    public PowerUpSystem() {
        filter.require(SpeedComponent.class);

        // Put all strategies TODO
        powerUpStrategies.put(0, new SpeedUpPowerUp());
    }

    @Override
    public void updateEntity(Entity entity) {

        SpeedComponent speed = (SpeedComponent) entity.getComponent(SpeedComponent.class);

        if (speed.getSpeedBoostTimer() > 0) {
            float delta = Gdx.graphics.getDeltaTime();
            speed.speedBoostTimer -= delta;

            if (speed.getSpeedBoostTimer() <= 0) {
                speed.setCurrentSpeed(speed.getBaseSpeed());
                speed.setSpeedBoostTimer(0);
            }
        }
    }

    private void kill(Entity entity) {
        LevelController.getInstance().handleRemovalRequests(entity);
        //some other logic TODO
    }

    @Override
    public void setContactStrategies() {
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);
        centralContactListener.subscribe(
            PlayerEntity.class, PowerUpEntity.class,
            null, // For beginContact
            this::applyPowerUp);
    }

    /**contact strategy between playerEntity and Ballentity */
    private void applyPowerUp(ContactSystem.CollisionEvent collisionEvent) {
        Gdx.app.log("poop" ,"collision" );
        PlayerEntity player;
        Entity powerUpEntity;

        if (collisionEvent.entityA instanceof PlayerEntity) {
            player = (PlayerEntity) collisionEvent.entityA;
            powerUpEntity = collisionEvent.entityB;
        } else {
            player = (PlayerEntity) collisionEvent.entityB;
            powerUpEntity = collisionEvent.entityA;
        }

        PowerUpComponent powerUp = (PowerUpComponent) powerUpEntity.getComponent(PowerUpComponent.class);

        applyPowerUpToPlayer(powerUp.getPowerUpType(), player);
        kill(powerUpEntity);
    }

    public void applyPowerUpToPlayer(int powerUpType, PlayerEntity player) {
        PowerUpStrategy strategy = powerUpStrategies.get(powerUpType);
        Gdx.app.log("poop" ,"collision" );
        if (strategy != null) {
            strategy.apply(player);
        }
    }


    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
