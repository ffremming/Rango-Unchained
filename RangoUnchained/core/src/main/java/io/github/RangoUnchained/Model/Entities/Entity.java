package io.github.RangoUnchained.Model.Entities;

import io.github.RangoUnchained.Model.Components.Component;

public interface Entity {
    public Component getComponent(Class<? extends Component> componentClass);

}
