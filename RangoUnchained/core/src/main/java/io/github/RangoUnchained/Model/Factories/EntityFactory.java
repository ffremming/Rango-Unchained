package io.github.RangoUnchained.Model.Factories;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class EntityFactory {
    public static PlayerEntity createPlayerEntity(BodyDef bodyDef, String spritePath) {
        BodyComponent body = new BodyComponent(bodyDef);

        SpriteComponent sprite = new SpriteComponent(spritePath);

        InputComponent input = new InputComponent();

        PlayerEntity player = new PlayerEntity(body, sprite, input);

        return player;
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
