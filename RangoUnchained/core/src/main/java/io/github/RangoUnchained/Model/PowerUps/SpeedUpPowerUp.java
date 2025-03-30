package io.github.RangoUnchained.Model.PowerUps;

import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class SpeedUpPowerUp implements PowerUpStrategy {

    @Override
    public void apply(Entity entity) {
        PlayerEntity player;

        if (entity instanceof PlayerEntity) {
            player = (PlayerEntity) entity;
        } else {
            System.out.println("cant apply speedPowerUp to non-player");
            return;
        }

        SpeedComponent speed = (SpeedComponent) player.getComponent(SpeedComponent.class);

        if (speed != null) {
            speed.setCurrentSpeed(speed.getCurrentSpeed() * 2);
            speed.setSpeedBoostTimer(5);
        }
    }
}
