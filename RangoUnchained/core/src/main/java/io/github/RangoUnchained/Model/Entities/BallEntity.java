package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.PhysicsComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;


public class BallEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public BallEntity(BodyComponent bodyComponent, StatComponent statComponent, SpriteComponent spriteComponent) {
        addComponent(bodyComponent);
        addComponent(statComponent);
        addComponent(spriteComponent);
        PhysicsComponent phyComp = new PhysicsComponent();
        phyComp.setContactLock(30);
        addComponent(phyComp);
       
    }
    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }
    private <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
