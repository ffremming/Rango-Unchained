package io.github.RangoUnchained.Model.PowerUps;

import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class HealthUpPowerUp implements PowerUpStrategy {
    @Override
    public void apply(Entity entity) {
        PlayerEntity player;

        if (entity instanceof PlayerEntity) {
            player = (PlayerEntity) entity;
        } else {
            System.out.println("cant apply healthUpPowerUp to non-player");
            return;
        }

        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);

        if (health != null) {
            health.addHealth(2);
        }
    }

    @Override
    public void remove(Entity entity) {
        //TODO: healthUp trenger vel ikke egt en remove?
        //HealthComponent health = (HealthComponent) entity.getComponent(HealthComponent.class);

    }
}
