package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;

public class MovementSystem implements Systems {

    private List<Entity> entities = new ArrayList<>();

    // Updates every playable entity's position based on input and velocity
    // Method called from controllers for updates
    public void updateEntityPosition() {
        for (Entity e : entities) {
            BodyComponent bodyComponent = (BodyComponent) e.getComponent(BodyComponent.class);
            InputComponent inputComponent = (InputComponent) e.getComponent(InputComponent.class);
            SpriteComponent spriteComponent = (SpriteComponent) e.getComponent(SpriteComponent.class);
            Body body = bodyComponent.getBody();

            if (inputComponent.isShoot()) {
                bodyComponent.getBody().setLinearVelocity(0, 0);
                spriteComponent.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
//                EntityFactory.createProjectileEntity(body.getPosition().x, body.getPosition().y, "Rango/Tounge.gif", );
                System.out.println("SPACE");
            } else if (inputComponent.isLeft()) {
                bodyComponent.getBody().setLinearVelocity(-500, 0);
                spriteComponent.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
                System.out.println("Left");
            } else if (inputComponent.isRight()){
                bodyComponent.getBody().setLinearVelocity(500, 0);
                spriteComponent.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
                System.out.println("Right");
            } else {
                bodyComponent.getBody().setLinearVelocity(0, 0);
                spriteComponent.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
            }
        }
    }

    public void addEntity(Entity entity) {
        if (entity.getComponent(BodyComponent.class) == null) {
            System.out.println("Entity did not have required bodycomponent");
            return;
        }
        if (entity.getComponent(InputComponent.class) == null) {
            System.out.println("Entity did not have required inputcomponent");
            return;
        }
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

    @Override
    public void clearSystems() {
        entities.clear();
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
