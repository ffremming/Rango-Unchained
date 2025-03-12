package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.PositionComponent;
import io.github.RangoUnchained.Model.Components.VelocityComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class MovementSystem {

    private List<Entity> entities = new ArrayList<>();

    public void updateEntityPosition() {
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

    /*public static void main(String[] args) {
        PositionComponent positionComponent = new PositionComponent();
        Entity player = new PlayerEntity();
        MovementSystem movementSystem = new MovementSystem();
        player.addComponent(positionComponent);
        movementSystem.addEntity(player);
        System.out.println(player.getComponent(PositionComponent.class));
        System.out.println("HEi");
    }*/

}
