package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.Component;

public class PlayerEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }

    @Override
    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
