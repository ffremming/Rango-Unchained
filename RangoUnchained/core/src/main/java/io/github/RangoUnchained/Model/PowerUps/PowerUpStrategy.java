package io.github.RangoUnchained.Model.PowerUps;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public interface PowerUpStrategy {
    void apply(Entity entity);
    void remove(Entity entity);
}
