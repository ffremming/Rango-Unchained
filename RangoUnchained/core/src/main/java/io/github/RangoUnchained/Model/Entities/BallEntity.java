package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BallComponent;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.BounceComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.StatComponent;

public class BallEntity extends Entity{

    public BallEntity(BodyComponent bodyComponent, StatComponent statComponent, SpriteComponent spriteComponent, BounceComponent bounceComp, BallComponent ballComp) {
        addComponent(bodyComponent);
        addComponent(statComponent);
        addComponent(spriteComponent);
        ContactComponent phyComp = new ContactComponent();
        phyComp.setContactLock(60);
        addComponent(phyComp);
        addComponent(bounceComp);
        addComponent(ballComp);
    }
}
