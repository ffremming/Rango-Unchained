package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class LifeTimeSystem implements Systems{

    ArrayList<Entity> entities;
    @Override
    public void clearSystems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearSystems'");
    }

    public void update(List<Entity> entities,LevelController levelController) {
        List<Entity> removeEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getComponent(LifeTimeComponent.class) == null) {
                continue;
            }
            LifeTimeComponent lifeTimeComponent = (LifeTimeComponent) entity.getComponent(LifeTimeComponent.class);
            lifeTimeComponent.decrementLifeTime();
            if (lifeTimeComponent.getLifeTime() <= 0) {
                //TODO must be changed after integration
                removeEntities.add(entity);
            }
        }

        levelController.getEntities().removeAll(removeEntities);

    }
}
