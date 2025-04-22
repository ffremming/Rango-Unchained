package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.PowerUpEntity;
import io.github.RangoUnchained.Model.PowerUps.HealthUpPowerUp;
import io.github.RangoUnchained.Model.PowerUps.PowerUpStrategy;
import io.github.RangoUnchained.Model.PowerUps.ShieldPowerUp;
import io.github.RangoUnchained.Model.PowerUps.SpeedUpPowerUp;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategy;

/**
 * System that manages power-up effects and expiration.
 */
public class PowerUpSystem implements Systems, ContactStrategy {
    private ComponentFilter filter = new ComponentFilter();
    private Map<Integer, PowerUpStrategy> powerUpStrategies = new HashMap<>();

    /**
     * Constructs the {@link PowerUpSystem} and registers available power-up strategies.
     */
    public PowerUpSystem() {
        filter.require(PowerUpComponent.class);

        // Put all strategies
        powerUpStrategies.put(0, new SpeedUpPowerUp());
        powerUpStrategies.put(1, new ShieldPowerUp());
        powerUpStrategies.put(2,new HealthUpPowerUp());
    }

    @Override
    public void updateEntity(Entity entity, float delta) {

        if (entity instanceof PlayerEntity){
            PowerUpComponent playerUp = (PowerUpComponent) entity.getComponent(PowerUpComponent.class);
            updatePowerUps(playerUp.getActivePowerUps(),entity);
        }
    }

    public void updatePowerUps(Map<Integer, Float> powerups, Entity entity) {
        powerups.entrySet().removeIf(entry -> {
            int powerUpType = entry.getKey();
            float remainingTime = entry.getValue() - Gdx.graphics.getDeltaTime();
            if (remainingTime <= 0) {
                PowerUpStrategy strategy = powerUpStrategies.get(powerUpType);
                if (strategy != null) {
                    strategy.remove(entity);
                }
                return true;
            } else {
                entry.setValue(remainingTime);
                return false;
            }
        });
    }

    private void kill(Entity entity) {
        LevelController.getInstance().handleRemovalRequests(entity);
    }

    @Override
    public void setContactStrategies() {
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);
        centralContactListener.subscribe(
            PlayerEntity.class, PowerUpEntity.class,
            this::applyPowerUp,
            null);
    }

    /**contact strategy between playerEntity and PowerUpEntity */
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
