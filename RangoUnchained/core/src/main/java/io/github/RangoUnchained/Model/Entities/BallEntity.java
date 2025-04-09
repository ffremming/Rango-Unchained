package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.AudioComponent;
import io.github.RangoUnchained.Model.Components.BallComponent;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.BounceComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;

public class BallEntity implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public BallEntity(BodyComponent bodyComponent, StatComponent statComponent, SpriteComponent spriteComponent, BounceComponent bounceComp, BallComponent ballComp) {
        addComponent(bodyComponent);
        addComponent(statComponent);
        addComponent(spriteComponent);
        ContactComponent phyComp = new ContactComponent();
        phyComp.setContactLock(60);
        addComponent(phyComp);
        addComponent(new AudioComponent(AudioComponent.EntityType.BALL));
        addComponent(bounceComp);
        addComponent(ballComp);
    }

    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return components.get(componentClass);
    }
    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }
}
