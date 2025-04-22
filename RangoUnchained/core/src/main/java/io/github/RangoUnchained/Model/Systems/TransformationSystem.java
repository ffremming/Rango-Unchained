package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Views.Utils.Constants;

/**
 * System that dynamically transforms (scales and repositions) entities over time.
 */
public class TransformationSystem implements Systems {

    private ComponentFilter filter = new ComponentFilter();

    public TransformationSystem() {
        filter
        .require(TransformationComponent.class)
        .require(BodyComponent.class)
        .require(SpriteComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float delta) {

        TransformationComponent transComp = (TransformationComponent) entity.getComponent(TransformationComponent.class);
        BodyComponent bodyComp = (BodyComponent) entity.getComponent(BodyComponent.class);
        SpriteComponent spriteComp = (SpriteComponent) entity.getComponent(SpriteComponent.class);

        Sprite sprite = spriteComp.getSprite();
        Body body = bodyComp.getBody();
        Fixture oldFixture = body.getFixtureList().first();

        if (transComp.getDuration()>0){

            if (transComp.getType() == TransformationComponent.RECTANGLE) {
                scaleRectangle(transComp, body, oldFixture, sprite);
            }
        }
        decrementDuration(transComp);
    }

    private void decrementDuration(TransformationComponent transComp){
        transComp.decrementDuration();

        if (transComp.getDuration()<=0){
            if (transComp.getPause()>0){
                transComp.decrementPauser();
            } else {

                if (transComp.getReverse()){
                    if (!transComp.isReversed()){
                        transComp.setTransformationStepsReverse();
                        transComp.setDuration(transComp.getLifeTime());

                    }else if (transComp.getAlwaysReverse()){
                        transComp.setTransformationSteps();
                        transComp.setDuration(transComp.getLifeTime());
                    }
                }
            }
        }
    }

    private void scaleRectangle(TransformationComponent transComp, Body body, Fixture oldFixture, Sprite sprite) {
        // Cast old shape to PolygonShape
        PolygonShape oldShape = (PolygonShape) oldFixture.getShape();

        // Extract rectangle vertices
        Vector2 vertex = new Vector2();
        oldShape.getVertex(0, vertex);
        float halfWidth = Math.abs(vertex.x);
        float halfHeight =  Math.abs(vertex.y);

        // Compute new dimensions
        float newHalfWidth = (float)(halfWidth*transComp.getTransformationWidthStep());
        float newHalfHeight = (float)(halfHeight*transComp.getTransformationHeightStep());

        // Prevent shape from getting too small
        float minSize = 0.1f;
        newHalfWidth = Math.max(newHalfWidth, minSize);
        newHalfHeight = Math.max(newHalfHeight, minSize);


        int direction = transComp.getDirection(); // Assuming 'UP' or 'DOWN' can be set as the direction
        float offsetX = 0, offsetY = 0;

        if (direction == TransformationComponent.UP) {
            // For UP scaling, we adjust the body position upwards while keeping the bottom fixed
            offsetY = (newHalfHeight - halfHeight);  // Difference in height
            body.setTransform(body.getPosition().x, body.getPosition().y + offsetY, body.getAngle());
        } else if (direction == TransformationComponent.DOWN) {
            // For DOWN scaling, we adjust the body position downwards while keeping the bottom fixed
            offsetY = (newHalfHeight - halfHeight);  // Difference in height
            body.setTransform(body.getPosition().x, body.getPosition().y - offsetY, body.getAngle());
        } else if (direction == TransformationComponent.LEFT) {
            // For LEFT scaling, adjust the body position leftwards while keeping the left fixed
            offsetX = (newHalfWidth - halfWidth);  // Difference in width
            body.setTransform(body.getPosition().x - offsetX, body.getPosition().y, body.getAngle());
        } else if (direction == TransformationComponent.RIGHT) {
            // For RIGHT scaling, adjust the body position rightwards while keeping the left fixed
            offsetX = (newHalfWidth - halfWidth);  // Difference in width
            body.setTransform(body.getPosition().x + offsetX, body.getPosition().y, body.getAngle());
        }

        // Transform the sprite size and position
        sprite.setSize(newHalfWidth * Constants.PPM*2 , newHalfHeight * Constants.PPM *2);
        //sprite.setPosition((body.getPosition().x*Constants.PPM  - ((newHalfWidth*Constants.PPM ))/2), (body.getPosition().y*Constants.PPM - newHalfHeight*Constants.PPM)/2);
        Gdx.app.log("scale",newHalfWidth+ ",height"+newHalfHeight + ",offsetX "+offsetX+ ",offsetY" + offsetY );

        // Remove old fixture
        body.destroyFixture(oldFixture);

        // Create a new polygon shape with scaled size
        PolygonShape newShape = new PolygonShape();
        newShape.setAsBox(newHalfWidth, newHalfHeight);

        // Create new fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = newShape;
        fixtureDef.density = oldFixture.getDensity();
        fixtureDef.friction = oldFixture.getFriction();
        fixtureDef.restitution = oldFixture.getRestitution();

        // Attach the new fixture to the same body
        body.createFixture(fixtureDef);
        newShape.dispose(); // Prevent memory leak
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
