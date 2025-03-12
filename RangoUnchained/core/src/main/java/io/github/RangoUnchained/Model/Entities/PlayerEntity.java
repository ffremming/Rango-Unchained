package core.src.main.java.io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import core.src.main.java.io.github.RangoUnchained.Model.Components.Component;
import core.src.main.java.io.github.RangoUnchained.Model.Components.InputComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Components.PositionComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Components.VelocityComponent;

public class PlayerEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public PlayerEntity() {
    }

    public PositionComponent getPositionComponent() {
        return (PositionComponent) components.get(PositionComponent.class);
    }

    public VelocityComponent getVelocityComponent() {
        return (VelocityComponent) components.get(VelocityComponent.class);
    }

    /*public InputComponent getInputComponent() {
        return inputComponent;
    }*/

    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }

    @Override
    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public static void main(String[] args) {
        PlayerEntity player = new PlayerEntity();
        PositionComponent positionComponent = new PositionComponent();
        player.addComponent(positionComponent);
        System.out.println(player.components.values());
    }
}
