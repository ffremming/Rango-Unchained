package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.PositionComponent;
import io.github.RangoUnchained.Model.Components.VelocityComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class CollisionSystem {

    private List<Entity> entities = new ArrayList<>();

    public void Collision() {
        for (Entity e : entities) {
            BodyComponent bodyComponent = (BodyComponent) e.getComponent(BodyComponent.class);
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

}
