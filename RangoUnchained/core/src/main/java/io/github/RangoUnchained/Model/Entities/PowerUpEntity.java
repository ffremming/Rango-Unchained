package io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;

public class PowerUpEntity extends Entity {

    public PowerUpEntity(BodyComponent bodyComponent, SpriteComponent spriteComponent,
                         PowerUpComponent powerUpComponent) {
        addComponent(powerUpComponent);
        addComponent(bodyComponent);
        addComponent(spriteComponent);
        addComponent(new ContactComponent());
        addComponent(new SpeedComponent(5f));
    }

    public PowerUpEntity() {
        //TODO Auto-generated constructor stub
    }
}
