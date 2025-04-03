package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;

public class PlayerEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public PlayerEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent, InputComponent inputComponent, HealthComponent healthComponent) {
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(inputComponent);
        addComponent(new ContactComponent());
        addComponent(healthComponent);
        //addComponent(new TransformationComponent(2, 2, 2, 160,TransformationComponent.RECTANGLE));
    }
    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
