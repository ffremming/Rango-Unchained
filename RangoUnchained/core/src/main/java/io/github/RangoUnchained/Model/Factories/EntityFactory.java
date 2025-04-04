package io.github.RangoUnchained.Model.Factories;
import io.github.RangoUnchained.Model.Components.BallComponent;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.BounceComponent;
import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;
import io.github.RangoUnchained.Model.Components.TutorialComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.BasicEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.FloorEntity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.PowerUpEntity;
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
        public static final short CATEGORY_POWERUP    = 0x0010; // New category for entities

        //masks for what should collide with what
        public static final short MASK_PLAYER     = CATEGORY_BALL | CATEGORY_OBSTACLE |CATEGORY_POWERUP ;
        public static final short MASK_BALL       = CATEGORY_PLAYER | CATEGORY_OBSTACLE | CATEGORY_PROJECTILE;
        public static final short MASK_OBSTACLE   = CATEGORY_PLAYER | CATEGORY_BALL |CATEGORY_POWERUP;
        public static final short MASK_PROJECTILE = CATEGORY_BALL;
        public static final short MASK_POWERUP    = CATEGORY_PLAYER | CATEGORY_OBSTACLE; // New mask for the new category


    private EntityFactory() {}

    public static Entity createEntity(float x, float y, String name, World world, Vector2 velocity, int hp, int level) {

        Gdx.app.log("entity factory", "Name: " + name);

        if (name.equals("Player")) {
            return createPlayerEntity(x, y, world, hp, level);

        } else if (name.startsWith("Ball")) {
            return createBallEntity(x, y, name, world,velocity);

        } else if (name.startsWith("Obsticle")) {
            return createObstacleEntity(name, world,x,y);

        } else if (name.startsWith("Projectile")) {
            return createProjectileEntity(x, y, "tounge/Tongue-3.png", world);
        }
        else if (name.startsWith("Background")) {
            return createBackground();
        } else if (name.startsWith("SpeedPowerUp")) {
            //TODO: change sprite to actual sprite
            return createPowerUp(x, y, "Powerup/Speed.png", world, 0,velocity);
        } else if (name.startsWith("ShieldPowerUp")) {
            //TODO: change sprite to actual sprite
            return createPowerUp(x, y, "Powerup/Shield.png", world, 1,velocity);
        }else if (name.startsWith("sizePowerUp")) {
            //TODO: change sprite to actual sprite
            return createPowerUp(x, y, "Powerup/Shield.png", world, 2,velocity);
        }


        // More entity types can be added here
        return null;
    }

    private static Entity createPowerUp(float x, float y, String spritePath, World world, int powerUpTyp, Vector2 velocity) {

        SpriteComponent sprite = new SpriteComponent(spritePath,64,64);

        float width = (sprite.getSprite().getWidth()/ Constants.PPM);
        float height = (sprite.getSprite().getHeight()/ Constants.PPM);

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createNoxBounceBoxFixture(width, height, CATEGORY_POWERUP, MASK_POWERUP), true);
        body.getBody().setLinearVelocity(velocity);
        PowerUpComponent powerUpComponent = new PowerUpComponent(powerUpTyp);
        PowerUpEntity powerUp = new PowerUpEntity(body, sprite, powerUpComponent);
        body.getBody().setUserData(powerUp);
        return powerUp;
    }

    // Player Entity
    public static PlayerEntity createPlayerEntity(float x, float y, World world, int hp, int level) {
        SpriteComponent sprite = new SpriteComponent("Rango/Rango.png",86,128);

        float width = (float)(sprite.getSprite().getWidth()/ Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight()/ Constants.PPM);

        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createNoxBounceBoxFixture(width, height,CATEGORY_PLAYER,MASK_PLAYER),true);


        InputComponent input = new InputComponent();

        HealthComponent health = hp <= 0 ? new HealthComponent(8) : new HealthComponent(hp);

        PlayerEntity player = new PlayerEntity(body, sprite, input, health);
        if (level == 0){
            player.addComponent(new TutorialComponent());
        }
        body.getBody().setUserData(player);
        return player;
    }

    private static final int BIGBALLRADIUS = 64;
    private static final int MEDIUMBALLRADIUS = 48;
    private static final int SMALLBALLRADIUS = 32;

    private static final int BIGBALLPOPPED = 0;
    private static final int MEDIUMBALLPOPPED = 1;
    private static final int SMALLBALLPOPPED = 2;

    private static final float SMALLPULSE = 0.005f;
    private static final float MEDIUMPULSE = 0.01f;
    private static final float BIGPULSE = 0.03f;






     // general method for creating all balls - called from factory method
     public static BallEntity createBallEntity(float x, float y, String name, World world, Vector2 velocity) {

        //String size = name.endsWith("Big") ? "plant" : name.endsWith("Medium") ? "armedillo" : "tumbleweed";
        int type = name.contains("Armedillo") ? BallComponent.ARMEDILLOTYPE : name.contains("Tumbleweed") ? BallComponent.TUMBLEWEEDTYPE : name.contains("Cactus") ? BallComponent.CACTUSTYPE : BallComponent.ARMEDILLOTYPE;
        int timesPopped = name.endsWith("Big") ? BIGBALLPOPPED : name.endsWith("Medium") ? MEDIUMBALLPOPPED : SMALLBALLPOPPED;
        int bounceType = name.endsWith("Big") ? BounceComponent.HIGH : name.endsWith("Medium") ? BounceComponent.MEDIUM : BounceComponent.LOW;
        return createSpecificBall(x, y, type,timesPopped,bounceType,name,world,velocity);
    }

    public static BallEntity createSpecificBall(float x,float y,int type, int timesPopped,int bounceType,String name,World world, Vector2 velocity){
        float radius = timesPopped==0 ? BIGBALLRADIUS : timesPopped==1 ? MEDIUMBALLRADIUS : SMALLBALLRADIUS;
        BounceComponent bounceComp = new BounceComponent(bounceType);
        StatComponent stats = new StatComponent();
        stats.setTimesPopped(timesPopped);

        String spriteName = name.split(" ")[1];

        SpriteComponent sprite = new SpriteComponent("Balls/"+spriteName+".png",radius,radius);

        float width = (float)(sprite.getSprite().getWidth() / Constants.PPM);
        float height = (float)(sprite.getSprite().getHeight() / Constants.PPM);
        BodyComponent body = createBody(world, x, y, BodyDef.BodyType.DynamicBody, createCircleFixture(width/2,CATEGORY_BALL,MASK_BALL),false);
        body.getBody().setLinearVelocity(velocity);
        body.getBody().setAngularDamping(0f);

        float pulse = timesPopped==0 ? BIGPULSE : timesPopped==1 ? MEDIUMPULSE :SMALLPULSE;
        body.getBody().applyAngularImpulse(pulse, true);
        BallComponent ballComp = new BallComponent(type);

        BallEntity ball = new BallEntity(body, stats, sprite,bounceComp,ballComp);
        body.getBody().setUserData(ball);

        return ball;
    }



    public static BasicEntity createBackground(){
        SpriteComponent sprite = new SpriteComponent("Background/Background.png",(int)(Gdx.graphics.getWidth()),(int)(Gdx.graphics.getHeight()));
        BasicEntity bg = new BasicEntity();
        bg.addComponent(sprite);
        return bg;
    }


    // 🔹 Obstacle Entity
    public static ObstacleEntity createObstacleEntity(String name, World world, float givenX, float givenY) {

        BodyComponent body = null;
        SpriteComponent sprite = null;
        float width = 0;
        float height = 0;
        int x = 0;
        int y = 0;

        float pxl = 1.9f;


        if (name.endsWith("Left") || name.endsWith("Right")){
            if (name.endsWith("Right")){
                x = Gdx.graphics.getWidth();
            } else {x = 0;}
            y = 0;
            height = Gdx.graphics.getHeight();
            width = 32/Constants.PPM;
            height = 1000/Constants.PPM*2;
            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height, CATEGORY_OBSTACLE,MASK_OBSTACLE),true);
            sprite = new SpriteComponent("Background/red.png",width*Constants.PPM,height *Constants.PPM);
        }



        else if (name.endsWith("Roof")||name.endsWith("Floor")){
            if (name.endsWith("Roof")){
                y = Gdx.graphics.getHeight();
                height = 50/Constants.PPM;
            } else {
                y = 50;
                height = 200/Constants.PPM;
            }
            x =  Gdx.graphics.getWidth()/2;
            width = Gdx.graphics.getWidth()/Constants.PPM;

            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height, CATEGORY_OBSTACLE,MASK_OBSTACLE),true);
        } 

        else if (name.equals("Obsticle")){
            x = (int)givenX;
            y = (int)givenY;
            width = 100/Constants.PPM;
            height = 200/Constants.PPM;

            body = createBody(world, x, y, BodyDef.BodyType.StaticBody, createBoxFixture(width, height, CATEGORY_OBSTACLE,MASK_OBSTACLE),true);
        } else {
            Gdx.app.log("EntityFactory", "Unknown obstacle type: " + name);
            return null;
        }

        if (name.endsWith("Floor")){
            sprite = new SpriteComponent("Background/Floor.png",width*Constants.PPM,height*Constants.PPM);
        } else if (name.endsWith("Roof")){
            sprite = new SpriteComponent("Background/Roof.png",width*Constants.PPM,height*Constants.PPM);

        } else {
            sprite = new SpriteComponent("Background/Wall.png",width*Constants.PPM,height*Constants.PPM);
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

