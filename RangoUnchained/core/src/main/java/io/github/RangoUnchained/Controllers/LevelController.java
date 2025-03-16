package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.MovementSystem;
import io.github.RangoUnchained.Model.Systems.PhysicsSystem;
import io.github.RangoUnchained.Model.Systems.SimpleBodyFactory;

public class LevelController {

    private static LevelController levelController;
    private List<Entity> entities;
    private MovementSystem movementSystem;
    private PhysicsSystem physicsSystem;
    private InputSystem inputSystem;
    private LevelController () {
        entities = new ArrayList<>();
        movementSystem = new MovementSystem();
        physicsSystem = new PhysicsSystem(-10);
        inputSystem = new InputSystem();
    }
    public static LevelController getInstance(){
        if (levelController == null) {
            levelController = new LevelController();
        }
        return levelController;
    }

    public List<Entity> getEntities() {
        return entities;
    }
    public void clearSystems() {
        World world = physicsSystem.getWorld();
        for (Entity e : entities) {
            world.destroyBody(((BodyComponent) e.getComponent(BodyComponent.class)).getBody());
            ((SpriteComponent) e.getComponent(SpriteComponent.class)).getTexture().dispose();
        }
        physicsSystem.clearSystems();
        inputSystem.clearSystems();
        movementSystem.clearSystems();
        entities.clear();
    }

    // Skal ta inn JSON etterhvert (tar inn info om hvilke entiteter vi vil ha på hvert level)
    // Initializes systems with entities
    public void initializeSystems() {
        PlayerEntity player = EntityFactory.createPlayerEntity(200, 500, "Rango/Rango.png");
        physicsSystem.addEntity(player);
        inputSystem.addEntity(player);
        movementSystem.addEntity(player);
        entities.add(player);
        System.out.println("Initialiserte riktig");
    }

    public MovementSystem getMovementSystem() {
        return movementSystem;
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }
}
