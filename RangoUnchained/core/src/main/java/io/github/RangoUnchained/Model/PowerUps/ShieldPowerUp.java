package io.github.RangoUnchained.Model.PowerUps;

import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class ShieldPowerUp implements PowerUpStrategy {
    @Override
    public void apply(Entity entity) {
        PlayerEntity player;


        if (entity instanceof PlayerEntity) {
            player = (PlayerEntity) entity;
        } else {
            System.out.println("cant apply speedPowerUp to non-player");
            return;
        }

        PowerUpComponent powerUpComp = (PowerUpComponent) player.getComponent(PowerUpComponent.class);
        

        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);

        if (health != null) {
            health.setShieldActive(true);
        }
    }

    @Override
    public void remove(Entity entity) {
        HealthComponent health = (HealthComponent) entity.getComponent(HealthComponent.class);
        if (health != null) {
            health.setShieldActive(false);
        }
    }
}
