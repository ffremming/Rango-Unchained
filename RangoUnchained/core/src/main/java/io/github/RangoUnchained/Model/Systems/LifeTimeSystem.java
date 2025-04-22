package io.github.RangoUnchained.Model.Systems;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

/**
 * System that handles entity removal after their lifetime expires.
 */
public class LifeTimeSystem implements Systems {

    private ComponentFilter filter = new ComponentFilter();

    public LifeTimeSystem() {
        filter
        .require(LifeTimeComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float delta) {

        LifeTimeComponent lifeTimeComponent = (LifeTimeComponent) entity.getComponent(LifeTimeComponent.class);
        lifeTimeComponent.decrementLifeTime();
        if (lifeTimeComponent.getLifeTime() <= 0) {
            LevelController.getInstance().handleRemovalRequests(entity);
        }
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
