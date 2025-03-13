package io.github.RangoUnchained.Model.Factories;

import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.PositionComponent;
import io.github.RangoUnchained.Model.Components.VelocityComponent;
import io.github.RangoUnchained.Model.Entities.Ball;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class EntityFactory {
    public static PlayerEntity createPlayerEntity(int startX, int startY, int velocityX, int velocityY) {
        PositionComponent position = new PositionComponent();
        position.setPosX(startX);
        position.setPosY(startY);

        VelocityComponent velocity = new VelocityComponent(velocityX, velocityY);

        InputComponent input = new InputComponent();

        PlayerEntity player = new PlayerEntity();
        player.addComponent(position);
        player.addComponent(velocity);
        player.addComponent(input);

        return player;
    }

    public static Ball createBallEntity(int startX, int startY, int velocityX, int velocityY) {
        PositionComponent position = new PositionComponent();
        position.setPosX(startX);
        position.setPosY(startY);

        VelocityComponent velocity = new VelocityComponent(velocityX, velocityY);

        InputComponent input = new InputComponent();

        Ball ball = new Ball();
        ball.addComponent(position);
        ball.addComponent(velocity);
        ball.addComponent(input);

        return ball;
    }

    // Flere public entity-metoder på samme format. Følg logical view
}
