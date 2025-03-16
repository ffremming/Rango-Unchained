package io.github.RangoUnchained.Model.Factories;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class EntityFactory {

    private EntityFactory() {}
    public static PlayerEntity createPlayerEntity(float x, float y, String spritePath) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        BodyComponent body = new BodyComponent(bodyDef);

        SpriteComponent sprite = new SpriteComponent(spritePath);
        sprite.getSprite().setPosition(x, y);

        InputComponent input = new InputComponent();

        return new PlayerEntity(body, sprite, input);
    }

    public static BallEntity createBallEntity(BodyDef bodyDef, String spritePath) {
        BodyComponent body = new BodyComponent(bodyDef);

        StatComponent stat = new StatComponent();

        SpriteComponent sprite = new SpriteComponent(spritePath);

        BallEntity ball = new BallEntity(body, stat, sprite);

        return ball;
    }

    // Flere public entity-metoder på samme format. Følg logical view
}
