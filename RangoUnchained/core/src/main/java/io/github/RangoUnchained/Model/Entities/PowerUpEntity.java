package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;

public class PowerUpEntity implements Entity {

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public PowerUpEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent) {
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(new ContactComponent());
        addComponent(new HealthComponent());
        addComponent(new SpeedComponent(5f));
    }

    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }

    private <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
