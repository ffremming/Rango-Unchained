package io.github.RangoUnchained.Model.Factories;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;

public class EntityFactory {

    public static final float PIXELS_TO_METERS = 1 / 64f; // Define this in a constants file

    private EntityFactory() {}

    public static Entity createEntity(float x, float y, String name, World world, Vector2 velocity) {

        Gdx.app.log("entity factory", "Name: " + name);

        if (name.equals("Player")) {
            return createPlayerEntity(x, y, world);

        } else if (name.startsWith("Ball")) {
            return createBallEntity(x, y, name, world);

        } else if (name.startsWith("Obstacle")) {
            return createObstacleEntity(x, y, 64, 64, "Obstacles/Obstacle.png", BodyDef.BodyType.StaticBody, world);

        } else if (name.startsWith("Projectile")) {
            return createProjectileEntity(x, y, "tongue/3.png", world);
        }
        // More entity types can be added here
        return null;
    }

// Player Entity
public static PlayerEntity createPlayerEntity(float x, float y, World world) {
    BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createBoxFixture(32, 32));
    SpriteComponent sprite = new SpriteComponent("Rango/Rango.png");
    InputComponent input = new InputComponent();

    return new PlayerEntity(body, sprite, input);

}
    private static final int BIGBALLRADIUS = 20;
    private static final int MEDIUMBALLRADIUS = 15;
    private static final int SMALLBALLRADIUS = 10;

     // general method for creating all balls - called from factory method
     public static BallEntity createBallEntity(float x, float y, String name, World world) {
        float radius = name.endsWith("Big") ? BIGBALLRADIUS : name.endsWith("Medium") ? MEDIUMBALLRADIUS : SMALLBALLRADIUS;
        String path = name.endsWith("Big") ? "Big ball" : name.endsWith("Medium") ? "Medium ball" : "Small ball";

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createCircleFixture(radius));
        SpriteComponent sprite = new SpriteComponent("Balls/"+path+".png");
        StatComponent stats = new StatComponent();

        return new BallEntity(body, stats, sprite);
    }

    // 🔹 Obstacle Entity
    public static ObstacleEntity createObstacleEntity(float x, float y, float width, float height, String spritePath, BodyDef.BodyType bodyType, World world) {
        BodyComponent body = createBody(world, x, y, bodyType, createBoxFixture(width, height));
        SpriteComponent sprite = new SpriteComponent(spritePath);
        return new ObstacleEntity(body, sprite);
    }

    // 🔹 Projectile Entity
    public static ProjectileEntity createProjectileEntity(float x, float y, String spritePath, World world) {
        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.KinematicBody, createBoxFixture(10, 10));
        SpriteComponent sprite = new SpriteComponent(spritePath);
        LifeTimeComponent lifeTime = new LifeTimeComponent(40); // 1.5 seconds
        return new ProjectileEntity(body, sprite, lifeTime);
    }

    // Universal Body Creation Method
    private static BodyComponent createBody(World world, float x, float y, BodyDef.BodyType type, FixtureDef fixtureDef) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);

        BodyComponent bodyComponent = new BodyComponent(bodyDef);
        Body body = world.createBody(bodyDef);

        if (body == null) {
            throw new IllegalStateException("Failed to create body in the world");
        }

        body.createFixture(fixtureDef);
        bodyComponent.setBody(body);
        return bodyComponent;
    }

    private static FixtureDef createBoxFixture(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        return createFixture(shape, 1f, 0.4f, 0.5f); // Default values for density, friction, and restitution
    }

    private static FixtureDef createCircleFixture(float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        return createFixture(shape, 1f, 0.4f, 0.5f); // Default values for density, friction, and restitution
    }

    private static FixtureDef createFixture(Shape shape, float density, float friction, float restitution) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        return fixtureDef;
    }
}
