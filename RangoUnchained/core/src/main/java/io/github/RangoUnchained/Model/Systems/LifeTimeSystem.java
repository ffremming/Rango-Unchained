package io.github.RangoUnchained.Model.Systems;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class LifeTimeSystem implements System{


    public LifeTimeSystem() {
        filter
        .require(LifeTimeComponent.class);
    }

    @Override
    public void updateEntity(Entity entity) {
        LifeTimeComponent lifeTimeComponent = (LifeTimeComponent) entity.getComponent(LifeTimeComponent.class);
        lifeTimeComponent.decrementLifeTime();
        if (lifeTimeComponent.getLifeTime() <= 0) {
            LevelController.getInstance().getEntities().remove(entity);
        }
    }
}
