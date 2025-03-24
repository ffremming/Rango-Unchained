package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.Systems.AnimationSystem;
import io.github.RangoUnchained.Model.Systems.InputSystem;
import io.github.RangoUnchained.Model.Systems.MovementSystem;
import io.github.RangoUnchained.Model.Systems.PhysicsSystem;
import io.github.RangoUnchained.Model.Systems.SimpleBodyFactory;

public class LevelController {

    private static LevelController levelController;
    private List<Entity> entities;
    private World world;
    private MovementSystem movementSystem;
    private PhysicsSystem physicsSystem;
    private InputSystem inputSystem;
    private AnimationSystem animationSystem;
    private LevelController () {
        entities = new ArrayList<>();
        movementSystem = new MovementSystem();
        world = new World(new Vector2(0f,-10f), true);
        physicsSystem = new PhysicsSystem(world);
        inputSystem = new InputSystem();
        animationSystem = new AnimationSystem();
    }

    /* Hvordan skal vi legge til height og width. (Og sette det til spirte og body)
    * Dependencies:
    * - Simple burde bli kalt av Physics
    * - Simple : Height og width (Hvor skal det bli implementert)
    *
    * - Entity : Avhengig
    * */


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
        World world = levelController.getWorld();
        for (Entity e : entities) {
            world.destroyBody(((BodyComponent) e.getComponent(BodyComponent.class)).getBody());
            ((SpriteComponent) e.getComponent(SpriteComponent.class)).getTexture().dispose();
        }
        physicsSystem.clearSystems();
        inputSystem.clearSystems();
        movementSystem.clearSystems();
        animationSystem.clearSystems();
        entities.clear();
    }

    public void handleSpawnRequests() {
        for (PhysicsSystem.SpawnRequest request : physicsSystem.getSpawnRequests()) {
            BallEntity newBall = EntityFactory.createBallEntity(
                request.x, request.y, request.radius, request.spritePath, physicsSystem.getWorld(),
            request.timesPopped, request.velocity);

            physicsSystem.addEntity(newBall);
        }
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        physicsSystem.clearSpawnRequests();

    }

    public void removeEntities() {
        for (Entity e : physicsSystem.getRemovalQueue()) {
            entities.remove(e);

            Body body = ((BodyComponent) e.getComponent(BodyComponent.class)).getBody();
            if (body != null) {
                world.destroyBody(body);
            }
        }
        physicsSystem.clearRemovalQueue();
    }

    // Skal ta inn JSON etterhvert (tar inn info om hvilke entiteter vi vil ha på hvert level)
    // Initializes systems with entities
    public void initializeSystems() {
        PlayerEntity player = EntityFactory.createPlayerEntity(200, 500, "Rango/Rango.png", world);
        physicsSystem.addEntity(player);
        inputSystem.addEntity(player);
        animationSystem.addEntity(player);
        movementSystem.addEntity(player);
        entities.add(player);

        BallEntity ball = EntityFactory.createBallEntity(300, 500, 10f,"Balls/Big ball.png", world, 0, new Vector2());
        physicsSystem.addEntity(ball);
        entities.add(ball);

        System.out.println("Initialiserte riktig");
    }

    public void initializeWorld() {
        entities.add(EntityFactory.createObstacleEntity(Gdx.graphics.getWidth(), 0, -30f,Gdx.graphics.getHeight(), "Rango/Tongue.gif", BodyDef.BodyType.StaticBody, world)); // RIGHT WALL
        entities.add(EntityFactory.createObstacleEntity(0,Gdx.graphics.getHeight(), Gdx.graphics.getWidth(),-30f, "Rango/Tongue.gif", BodyDef.BodyType.StaticBody, world)); // ROOF
        entities.add(EntityFactory.createObstacleEntity(0,0, Gdx.graphics.getWidth(),30f, "Rango/Tongue.gif", BodyDef.BodyType.StaticBody, world)); // GROUND
        entities.add(EntityFactory.createObstacleEntity(0,0, 30f,Gdx.graphics.getHeight(), "Rango/Tongue.gif", BodyDef.BodyType.StaticBody, world)); // LEFT WALL
    }

    public MovementSystem getMovementSystem() {
        return movementSystem;
    }
    public AnimationSystem getAnimationSystem() {
        return animationSystem;
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public World getWorld() {
        return world;
    }
}
