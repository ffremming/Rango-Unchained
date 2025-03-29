package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;

public class PlayerEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public PlayerEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent, InputComponent inputComponent) {
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(inputComponent);
        addComponent(new ContactComponent());
        //addComponent(new TransformationComponent(2, 2, 2, 160,TransformationComponent.RECTANGLE));
    }
    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }

    private <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
