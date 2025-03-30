package io.github.RangoUnchained.Model.Factories;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.BounceComponent;
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
import io.github.RangoUnchained.Model.Entities.FloorEntity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Views.Utils.Constants;

public class EntityFactory {

    public static final float PIXELS_TO_METERS = 1 / 64f; // Define this in a constants file
    public static final float METERS_TO_PIXELS = 64f / 1; // Define this in a constants file

    //categories
    public static final short CATEGORY_PLAYER     = 0x0001;
    public static final short CATEGORY_BALL       = 0x0002;
    public static final short CATEGORY_OBSTACLE   = 0x0004;
    public static final short CATEGORY_PROJECTILE = 0x0008;

    //masks for what should collide with what
    public static final short MASK_PLAYER     = CATEGORY_BALL | CATEGORY_OBSTACLE;         // Player ignores projectiles, for instance.
    public static final short MASK_BALL       = CATEGORY_PLAYER | CATEGORY_OBSTACLE | CATEGORY_PROJECTILE;       // Balls might ignore projectiles too.
    public static final short MASK_OBSTACLE   = CATEGORY_PLAYER | CATEGORY_BALL;
    public static final short MASK_PROJECTILE = CATEGORY_BALL;

    private EntityFactory() {}

    public static Entity createEntity(float x, float y, String name, World world, Vector2 velocity) {

        Gdx.app.log("entity factory", "Name: " + name);

        if (name.equals("Player")) {
            return createPlayerEntity(x, y, world);

        } else if (name.startsWith("Ball")) {
            return createBallEntity(x, y, name, world,velocity);

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

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createNoxBounceBoxFixture(width, height,CATEGORY_PLAYER,MASK_PLAYER),true);
        InputComponent input = new InputComponent();

        PlayerEntity player = new PlayerEntity(body, sprite, input);
        body.getBody().setUserData(player);
        return player;
    }

    private static final int BIGBALLRADIUS = 20;
    private static final int MEDIUMBALLRADIUS = 15;
    private static final int SMALLBALLRADIUS = 10;

    private static final int BIGBALLPOPPED = 0;
    private static final int MEDIUMBALLPOPPED = 1;
    private static final int SMALLBALLPOPPED = 2;


     // general method for creating all balls - called from factory method
     public static BallEntity createBallEntity(float x, float y, String name, World world, Vector2 velocity) {
        float radius = name.endsWith("Big") ? BIGBALLRADIUS : name.endsWith("Medium") ? MEDIUMBALLRADIUS : SMALLBALLRADIUS;
        String path = name.endsWith("Big") ? "Big ball" : name.endsWith("Medium") ? "Medium ball" : "Small ball";
        int timesPopped = name.endsWith("Big") ? BIGBALLPOPPED : name.endsWith("Medium") ? MEDIUMBALLPOPPED : SMALLBALLPOPPED;
        int bounceType = name.endsWith("Big") ? BounceComponent.HIGH : name.endsWith("Medium") ? BounceComponent.MEDIUM : BounceComponent.LOW;

        BounceComponent bounceComp = new BounceComponent(bounceType);

        //Stats
        StatComponent stats = new StatComponent();
        stats.setTimesPopped(timesPopped);

        //sprite
        SpriteComponent sprite = new SpriteComponent("Balls/"+path+".png",64,64);

        //body
        float width = (float)(sprite.getSprite().getWidth() / Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight() / Constants.PPM);
        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createCircleFixture(width/2,CATEGORY_BALL,MASK_BALL),true);
        body.getBody().setLinearVelocity(velocity);
        body.getBody().setLinearDamping(0);
        body.getBody().setAngularDamping(0);

        BallEntity ball = new BallEntity(body, stats, sprite,bounceComp);
        body.getBody().setUserData(ball);
        return ball;
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
            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height, CATEGORY_OBSTACLE,MASK_OBSTACLE),true);
            sprite = new SpriteComponent("Background/red.png",width*Constants.PPM,height *Constants.PPM);
        }

       

        else if (name.endsWith("Roof")||name.endsWith("Floor")){
            width = 733/Constants.PPM*2;
            height = 64/Constants.PPM;
            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height, CATEGORY_OBSTACLE,MASK_OBSTACLE),true);
            sprite = new SpriteComponent("Background/red.png",width*Constants.PPM,height*Constants.PPM);
        }
        ObstacleEntity obstacle;
        if (name.endsWith("Floor")){
            //uses floorentity for identification later (in physicssystem)
             obstacle = new FloorEntity(body, sprite);
        } else { 
            obstacle = new ObstacleEntity(body, sprite);
        }
        
        body.getBody().setUserData(obstacle);
        
        return new ObstacleEntity(body, sprite);
    }

    // 🔹 Projectile Entity
    public static ProjectileEntity createProjectileEntity(float x, float y, String spritePath, World world) {
        SpriteComponent sprite = new SpriteComponent(spritePath,4*4,32*3);//(20*METERS_TO_PIXELS),(int)(50*METERS_TO_PIXELS)

        float width = (float)(sprite.getSprite().getWidth() / Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight() / Constants.PPM);

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.KinematicBody, createBoxFixture(width, height,CATEGORY_PROJECTILE,MASK_PROJECTILE),true);
       
        LifeTimeComponent lifeTime = new LifeTimeComponent(50); // 1.5 seconds
        TransformationComponent transComp = new TransformationComponent(3, 1, 1, 10
        ,TransformationComponent.RECTANGLE,
        TransformationComponent.UP
        ,true,
        5
        );
        transComp.setAutoReverse(true);
        transComp.setAlwaysReverse(false);

        ProjectileEntity projectile = new ProjectileEntity(body, sprite, lifeTime,transComp);
        body.getBody().setUserData(projectile);
        return projectile;
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

    private static FixtureDef createNoxBounceBoxFixture(float width,float height, short category, short mask){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0f;
        // Set collision filtering:
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }

    private static FixtureDef createBoxFixture(float width, float height, short category, short mask) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0f;
        // Set collision filtering:
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }

    private static FixtureDef createCircleFixture(float radius, short category, short mask) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.99f;
        // Set collision filtering:
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }
}