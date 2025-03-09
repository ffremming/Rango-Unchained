package main.java.io.github.RangoUnchained.Model.Entities;

import main.java.io.github.RangoUnchained.Model.Components.InputComponent;
import main.java.io.github.RangoUnchained.Model.Components.PositionComponent;
import main.java.io.github.RangoUnchained.Model.Components.VelocityComponent;

public class PlayerEntity {

    private PositionComponent positionComponent;
    private VelocityComponent velocityComponent;
    private InputComponent inputComponent;

    public PlayerEntity(PositionComponent positionComponent,
                        VelocityComponent velocityComponent,
                        InputComponent inputComponent) {
        this.positionComponent = positionComponent;
        this.velocityComponent = velocityComponent;
        this.inputComponent = inputComponent;
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public VelocityComponent getVelocityComponent() {
        return velocityComponent;
    }

    public InputComponent getInputComponent() {
        return inputComponent;
    }
}
