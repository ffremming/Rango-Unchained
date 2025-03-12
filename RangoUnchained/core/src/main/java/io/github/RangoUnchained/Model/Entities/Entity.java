package core.src.main.java.io.github.RangoUnchained.Model.Entities;

import core.src.main.java.io.github.RangoUnchained.Model.Components.Component;
import core.src.main.java.io.github.RangoUnchained.Model.Components.ComponentMapper;

public interface Entity {
    Component getComponent(Class<? extends Component> componentClass);

    <T extends Component> void addComponent(T component);

}
