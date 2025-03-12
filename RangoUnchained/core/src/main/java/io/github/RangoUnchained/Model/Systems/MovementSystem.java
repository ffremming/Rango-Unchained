package core.src.main.java.io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.List;

import core.src.main.java.io.github.RangoUnchained.Model.Entities.Entity;

public class MovementSystem {

    private List<Entity> entities = new ArrayList<>();

    public MovementSystem() {}

    public void update() {
        for (Entity e : entities) {
            e.
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
