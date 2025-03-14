package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class PhysicsSystem {

    private List<Entity> entities = new ArrayList<>();
    private World world;

    public PhysicsSystem(float gravity) {
        world = new World(new Vector2(0, gravity), true);
    }

    public void addEntity(Entity entity) {
        BodyComponent bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        if (bodyComponent == null) {
            return;
        }
        world.createBody(bodyComponent.getBodyDef());
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

}
