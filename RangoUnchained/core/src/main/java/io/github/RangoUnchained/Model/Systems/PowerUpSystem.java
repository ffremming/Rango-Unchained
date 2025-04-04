package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.PowerUpEntity;
import io.github.RangoUnchained.Model.PowerUps.PowerUpStrategy;
import io.github.RangoUnchained.Model.PowerUps.ResizeballsStrategy;
import io.github.RangoUnchained.Model.PowerUps.ShieldPowerUp;
import io.github.RangoUnchained.Model.PowerUps.SpeedUpPowerUp;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategy;

public class PowerUpSystem implements System, ContactStrategy {
    private ComponentFilter filter = new ComponentFilter();
    private Map<Integer, PowerUpStrategy> powerUpStrategies = new HashMap<>();


    public PowerUpSystem() {
        filter.require(PowerUpComponent.class);

        // Put all strategies TODO
        powerUpStrategies.put(0, new SpeedUpPowerUp());
        powerUpStrategies.put(1, new ShieldPowerUp());
        powerUpStrategies.put(2,new ResizeballsStrategy());
    }

    @Override
    public void updateEntity(Entity entity) {

        if (entity instanceof PlayerEntity){
            PowerUpComponent playerUp = (PowerUpComponent) entity.getComponent(PowerUpComponent.class);
            updatePowerUps(playerUp.getActivePowerUps(),entity);
        }

        SpeedComponent speed = (SpeedComponent) entity.getComponent(SpeedComponent.class);
        /** 
        if (speed.getSpeedBoostTimer() > 0) {
            float delta = Gdx.graphics.getDeltaTime();
            speed.speedBoostTimer -= delta;

            if (speed.getSpeedBoostTimer() <= 0) {
                speed.setCurrentSpeed(speed.getBaseSpeed());
                speed.setSpeedBoostTimer(0);
            }
        }*/
    }

    public void updatePowerUps(Map<Integer, Float> powerups, Entity entity) {
        powerups.entrySet().removeIf(entry -> {
            int powerUpType = entry.getKey(); // Get the key (power-up type)
            float remainingTime = entry.getValue() - Gdx.graphics.getDeltaTime();
            if (remainingTime <= 0) {
                PowerUpStrategy strategy = powerUpStrategies.get(powerUpType);
                if (strategy != null) {
                    strategy.remove(entity); // Assuming a remove method exists in PowerUpStrategy
                }
                return true; // Remove expired power-up
            } else {
                entry.setValue(remainingTime); // Update remaining time
                return false;
            }
        });
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
            this::applyPowerUp,
            null);
    }

    /**contact strategy between playerEntity and Ballentity */
    private void applyPowerUp(ContactSystem.CollisionEvent collisionEvent) {
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
        PowerUpComponent playerUp = (PowerUpComponent) player.getComponent(PowerUpComponent.class);

        playerUp.addPowerUp(powerUp.getPowerUpType(),8);
        applyPowerUpToPlayer(powerUp.getPowerUpType(), player);
        kill(powerUpEntity);
    }

    public void applyPowerUpToPlayer(int powerUpType, PlayerEntity player) {
        PowerUpStrategy strategy = powerUpStrategies.get(powerUpType);
        if (strategy != null) {
            strategy.apply(player);
        }
    }


    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
