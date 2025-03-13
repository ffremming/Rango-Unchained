package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.PositionComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Components.VelocityComponent;


public class BallEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public BallEntity(VelocityComponent velocityComponent, PositionComponent positionComponent, StatComponent statComponent) {
        addComponent(velocityComponent);
        addComponent(positionComponent);
        addComponent(statComponent);
    }
    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }
    private <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
