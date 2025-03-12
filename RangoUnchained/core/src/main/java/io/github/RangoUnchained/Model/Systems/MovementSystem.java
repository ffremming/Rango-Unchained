package core.src.main.java.io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.List;

import core.src.main.java.io.github.RangoUnchained.Model.Components.InputComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Components.PositionComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Components.VelocityComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Entities.Entity;

public class MovementSystem {

    private List<Entity> entities = new ArrayList<>();

    public MovementSystem() {}

    public void update() {
        for (Entity e : entities) {
            PositionComponent positionComponent = (PositionComponent) e.getComponent(PositionComponent.class);
            VelocityComponent velocityComponent = (VelocityComponent) e.getComponent(VelocityComponent.class);
            InputComponent inputComponent = (InputComponent) e.getComponent(InputComponent.class);

            if (inputComponent.isShoot()) {
                return; //Play animation?
            } else if (inputComponent.isLeft()) {
                positionComponent.setPosX(positionComponent.getPosX() - velocityComponent.getVelocity());
            } else if (inputComponent.isRight()){
                positionComponent.setPosX(positionComponent.getPosX() + velocityComponent.getVelocity());
            }
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
