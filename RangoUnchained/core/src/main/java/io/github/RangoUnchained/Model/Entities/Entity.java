package io.github.RangoUnchained.Model.Entities;

import io.github.RangoUnchained.Model.Components.Component;

public interface Entity {
    Component getComponent(Class<? extends Component> componentClass);

    <T extends Component> void addComponent(T component);

}
