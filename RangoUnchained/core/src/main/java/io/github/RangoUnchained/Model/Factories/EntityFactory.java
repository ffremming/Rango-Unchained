package io.github.RangoUnchained.Model.Factories;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.SimpleBodyFactory;

public class EntityFactory {

    public static final float PIXELS_TO_METERS = 1 / 64f; // Define this in a constants file

    private EntityFactory() {}

    public static PlayerEntity createPlayerEntity(float x, float y, String spritePath, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);
        BodyComponent bodyComponent = new BodyComponent(bodyDef);
        bodyComponent.setBody(body);

        SpriteComponent spriteComponent = new SpriteComponent(spritePath);
        spriteComponent.getSprite().setPosition(x, y);
        float width = spriteComponent.getSprite().getWidth();
        float height = spriteComponent.getSprite().getHeight();

        InputComponent inputComponent = new InputComponent();

        PlayerEntity player = new PlayerEntity(bodyComponent, spriteComponent, inputComponent);
        body.setUserData(player); // Attach entity to the body

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        body.createFixture(fixtureDef);
        shape.dispose();

        return player;
    }

    public static BallEntity createBallEntity(float x, float y, float radius, String spritePath, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        BodyComponent bodyComponent = new BodyComponent(bodyDef);

        StatComponent statComponent = new StatComponent();

        SpriteComponent spriteComponent = new SpriteComponent(spritePath);
        spriteComponent.getSprite().setPosition(x, y);

        BallEntity ball = new BallEntity(bodyComponent, statComponent, spriteComponent);

        FixtureDef fixtureDef = new FixtureDef();
        Body body = world.createBody(bodyComponent.getBodyDef()); // Create body and attach to world from PhysicsSystem
        bodyComponent.setBody(body);
        body.setUserData(ball); // Attach entity to the body
        fixtureDef.shape = new CircleShape();
        fixtureDef.shape.setRadius(radius);
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef);

        return ball;
    }

    public static ObstacleEntity createObstacleEntity(float x, float y, float width, float height, String spritePath, BodyDef.BodyType bodyType, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x + width/2, y + height/2);

        BodyComponent bodyComponent = new BodyComponent(bodyDef);
        Body body = world.createBody(bodyComponent.getBodyDef()); // Create body and attach to world from PhysicsSystem
        bodyComponent.setBody(body);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        shape.dispose();

        SpriteComponent spriteComponent = new SpriteComponent(spritePath);

        Texture texture = spriteComponent.getTexture();
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteComponent.getSprite().setSize(width, height);
        spriteComponent.getSprite().setPosition(x, y);
        spriteComponent.getSprite().setRegion(0, 0, width / 64, height / 64);

        ObstacleEntity obstacleEntity = new ObstacleEntity(bodyComponent, spriteComponent);
        body.setUserData(obstacleEntity); // Attach entity to the body

        return obstacleEntity;
    }


    public static ProjectileEntity createProjectileEntity(float x, float y, String spritePath, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x * PIXELS_TO_METERS, y * PIXELS_TO_METERS); // Convert position to meters
    
        BodyComponent bodyComponent = new BodyComponent(bodyDef);
        Body body = world.createBody(bodyComponent.getBodyDef());
        bodyComponent.setBody(body);
    
        SpriteComponent spriteComponent = new SpriteComponent(spritePath);
        float spriteWidth = spriteComponent.getSprite().getWidth();
        float spriteHeight = spriteComponent.getSprite().getHeight();
    
        float scaledWidth = spriteWidth * 5;
        float scaledHeight = spriteHeight * 5;
    
        // Set sprite size
        spriteComponent.getSprite().setSize(scaledWidth, scaledHeight);
    
        // Set sprite position so it's centered on the body
        spriteComponent.getSprite().setPosition(x - scaledWidth / 2, y - scaledHeight / 2);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((scaledWidth / 2) * PIXELS_TO_METERS, (scaledHeight / 2) * PIXELS_TO_METERS); // Convert size to meters
    
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;
    
        body.createFixture(fixtureDef);
        shape.dispose();
    
        // Attach sprite to entity
        ProjectileEntity projectileEntity = new ProjectileEntity(bodyComponent, spriteComponent, new LifeTimeComponent(45));
        body.setUserData(projectileEntity);
    
        return projectileEntity;
    }
    
    

    // Flere public entity-metoder på samme format. Følg logical view
}