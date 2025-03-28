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
import io.github.RangoUnchained.Model.Components.TransformationComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.BasicEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Views.Utils.Constants;

public class EntityFactory {

    public static final float PIXELS_TO_METERS = 1 / 64f; // Define this in a constants file
    public static final float METERS_TO_PIXELS = 64f / 1; // Define this in a constants file


    private EntityFactory() {}

    public static Entity createEntity(float x, float y, String name, World world, Vector2 velocity) {

        Gdx.app.log("entity factory", "Name: " + name);

        if (name.equals("Player")) {
            return createPlayerEntity(x, y, world);

        } else if (name.startsWith("Ball")) {
            return createBallEntity(x, y, name, world);

        } else if (name.startsWith("Obsticle")) {
            return createObstacleEntity(x, y, name, world);

        } else if (name.startsWith("Projectile")) {
            return createProjectileEntity(x, y, "tongue/3Cropped.png", world);
        }
        else if (name.startsWith("Background")) {
            return createBackground();
        }


        // More entity types can be added here
        return null;
    }

    // Player Entity
    public static PlayerEntity createPlayerEntity(float x, float y, World world) {
        SpriteComponent sprite = new SpriteComponent("Rango/Rango.png",86,128);

        float width = (float)(sprite.getSprite().getWidth()/ Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight()/ Constants.PPM);

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createNoxBounceBoxFixture(width, height),true);
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
        
        SpriteComponent sprite = new SpriteComponent("Balls/"+path+".png",64,64);

        float width = (float)(sprite.getSprite().getWidth() / Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight() / Constants.PPM);
        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createCircleFixture(width/2),true);
        StatComponent stats = new StatComponent();

        body.getBody().setLinearDamping(0);
        body.getBody().setAngularDamping(0);
        return new BallEntity(body, stats, sprite);
    }

    public static BasicEntity createBackground(){
        SpriteComponent sprite = new SpriteComponent("Background/Background.png",(int)(800*1.9),(int)(480*1.9));
        BasicEntity bg = new BasicEntity();
        bg.addComponent(sprite);
        return bg;
    }


    // 🔹 Obstacle Entity
    public static ObstacleEntity createObstacleEntity(float x, float y, String name, World world) {

        BodyComponent body = null;
        SpriteComponent sprite = null;
        float width;
        float height;

        float pxl = 1.9f;

        if (name.endsWith("Left") || name.endsWith("Right")){
            width = 32/Constants.PPM;
            height = 1000/Constants.PPM;
            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height),true);
            sprite = new SpriteComponent("Background/red.png",width*Constants.PPM,height *Constants.PPM);
        }

       

        else if (name.endsWith("Roof")||name.endsWith("Floor")){
            width = 733/Constants.PPM*2;
            height = 64/Constants.PPM;
            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height),true);
            sprite = new SpriteComponent("Background/red.png",width*Constants.PPM,height*Constants.PPM);
        }

        
        return new ObstacleEntity(body, sprite);
    }

    // 🔹 Projectile Entity
    public static ProjectileEntity createProjectileEntity(float x, float y, String spritePath, World world) {
        SpriteComponent sprite = new SpriteComponent(spritePath,4*4,32*3);//(20*METERS_TO_PIXELS),(int)(50*METERS_TO_PIXELS)

        float width = (float)(sprite.getSprite().getWidth() / Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight() / Constants.PPM);

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.KinematicBody, createBoxFixture(width, height),true);
       
        LifeTimeComponent lifeTime = new LifeTimeComponent(50); // 1.5 seconds
        TransformationComponent transComp = new TransformationComponent(5, 1, 1, 20
        ,TransformationComponent.RECTANGLE,
        TransformationComponent.UP
        ,true,
        15
        );
        transComp.setAutoReverse(true);
        transComp.setAlwaysReverse(false);
        return new ProjectileEntity(body, sprite, lifeTime,transComp);
    }

    // Universal Body Creation Method
    private static BodyComponent createBody(World world, float x, float y, BodyDef.BodyType type, FixtureDef fixtureDef,boolean fixedRotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x/Constants.PPM, y/Constants.PPM);
        bodyDef.fixedRotation = fixedRotation;

        BodyComponent bodyComponent = new BodyComponent(bodyDef);
        Body body = world.createBody(bodyDef);

        if (body == null) {
            throw new IllegalStateException("Failed to create body in the world");
        }
        body.setLinearDamping(0f);

        body.createFixture(fixtureDef);
        bodyComponent.setBody(body);
        return bodyComponent;
    }

    private static FixtureDef createNoxBounceBoxFixture(float width,float height){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        return createFixture(shape, 1f, 0.4f, 0.5f); // Default values for density, friction, and restitution
    }

    private static FixtureDef createBoxFixture(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        return createFixture(shape, 1f, 0.4f, 0f); // Default values for density, friction, and restitution
    }

    private static FixtureDef createCircleFixture(float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        return createFixture(shape, 1f, 0.0f, 0.99f); // Default values for density, friction, and restitution
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