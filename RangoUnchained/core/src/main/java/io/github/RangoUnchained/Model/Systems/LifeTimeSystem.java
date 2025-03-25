package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class LifeTimeSystem implements System{

    private ComponentFilter filter = new ComponentFilter();

    public LifeTimeSystem() {
        filter
        .require(LifeTimeComponent.class);
    }

    @Override
    public void updateEntity(Entity entity) {

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
