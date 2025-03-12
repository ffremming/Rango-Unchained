package main.java.io.github.RangoUnchained.Model.Factories;

import main.java.io.github.RangoUnchained.Model.Components.InputComponent;
import main.java.io.github.RangoUnchained.Model.Components.PositionComponent;
import main.java.io.github.RangoUnchained.Model.Components.VelocityComponent;
import main.java.io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class EntityFactory {
    public static PlayerEntity createPlayerEntity(int startX, int startY, int velocityX, int velocityY) {
        PositionComponent position = new PositionComponent();
        position.setPosX(startX);
        position.setPosY(startY);

        VelocityComponent velocity = new VelocityComponent(velocityY, velocityX);

        InputComponent input = new InputComponent();

        PlayerEntity player = new PlayerEntity(position, velocity, input);

        return player;
    }

    // Flere public createXxxEntity-metoder på samme format. Følg logical view
}
