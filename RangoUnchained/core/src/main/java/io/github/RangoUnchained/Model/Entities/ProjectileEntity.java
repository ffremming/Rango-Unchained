package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;

public class ProjectileEntity extends Entity{

    public ProjectileEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent, LifeTimeComponent lifeTimeComponent,TransformationComponent transComp) {
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(lifeTimeComponent);
        addComponent(transComp);
        addComponent(new ContactComponent());
    }
}
