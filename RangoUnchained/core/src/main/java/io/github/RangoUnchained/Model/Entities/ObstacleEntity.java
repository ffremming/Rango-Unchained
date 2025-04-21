package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;

public class ObstacleEntity extends Entity {

    public ObstacleEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent) {
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(new ContactComponent());
    }

}
