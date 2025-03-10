package core.src.main.java.io.github.RangoUnchained.Model.Entities;

import core.src.main.java.io.github.RangoUnchained.Model.Components.InputComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Components.PositionComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Components.VelocityComponent;

public class PlayerEntity implements Entity{

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
