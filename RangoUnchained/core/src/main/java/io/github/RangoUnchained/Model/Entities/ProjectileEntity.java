package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;

public class ProjectileEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public ProjectileEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent, LifeTimeComponent lifeTimeComponent,TransformationComponent transComp) {
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(lifeTimeComponent);
        addComponent(transComp);
    }
    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }
    private <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
