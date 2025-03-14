package io.github.RangoUnchained.Model.Components;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public class BodyComponent implements Component {

    private Body body;
    private BodyDef bodyDef;

    // When creating a body component you need to create a bodydef to define starting point and type (Dynamic, Static, Kinematic)
    // See "tips" chat on discord for documentation of box2d. ctrl + f "bodies"
    public BodyComponent(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
