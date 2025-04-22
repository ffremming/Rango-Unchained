package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Views.Utils.Constants;

public class BodyFactory {
    
    // Universal Body Creation Method
    static BodyComponent createBody(World world, float x, float y, BodyDef.BodyType type, FixtureDef fixtureDef,boolean fixedRotation) {
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

    static FixtureDef createBoxFixture(float width, float height, short category, short mask) {
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

    static FixtureDef createCircleFixture(float radius, short category, short mask) {
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
