package io.github.RangoUnchained.Model.Factories;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Systems.SimpleBodyFactory;

public class EntityFactory {

    private EntityFactory() {}

    public static PlayerEntity createPlayerEntity(float x, float y, String spritePath, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        BodyComponent bodyComponent = new BodyComponent(bodyDef);

        SpriteComponent spriteComponent = new SpriteComponent(spritePath);
        spriteComponent.getSprite().setPosition(x, y);

        InputComponent inputComponent = new InputComponent();

        PlayerEntity player = new PlayerEntity(bodyComponent, spriteComponent, inputComponent);

        FixtureDef fixtureDef = new FixtureDef();
        Body body = world.createBody(bodyComponent.getBodyDef()); // Create body and attach to world from PhysicsSystem
        bodyComponent.setBody(body);
        body.setUserData(player); // Attach entity to the body
        fixtureDef.shape = new PolygonShape();
        fixtureDef.friction = 0.4f;
        body.createFixture(fixtureDef);

        return player;
    }

    public static BallEntity createBallEntity(float x, float y, String spritePath, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        BodyComponent bodyComponent = new BodyComponent(bodyDef);

        StatComponent stat = new StatComponent();

        SpriteComponent sprite = new SpriteComponent(spritePath);

        BallEntity ball = new BallEntity(bodyComponent, stat, sprite);

        FixtureDef fixtureDef = new FixtureDef();
        Body body = world.createBody(bodyComponent.getBodyDef()); // Create body and attach to world from PhysicsSystem
        body.setUserData(ball); // Attach entity to the body


        return ball;
    }

    public static ObstacleEntity createObstacleEntity(float x, float y, float width, float height, String spritePath, BodyDef.BodyType bodyType, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x, y);

        BodyComponent bodyComponent = new BodyComponent(bodyDef);
        Body body = world.createBody(bodyComponent.getBodyDef()); // Create body and attach to world from PhysicsSystem
        bodyComponent.setBody(body);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1000, 40);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);

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

    // Flere public entity-metoder på samme format. Følg logical view
}
