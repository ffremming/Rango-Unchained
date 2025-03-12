package core.src.main.java.io.github.RangoUnchained.Model.Entities;

import java.util.HashMap;
import java.util.Map;

import core.src.main.java.io.github.RangoUnchained.Model.Components.Component;

public class Ball implements Entity{

    private Map<Class<? extends Component>, Component> components = new HashMap<>();
    @Override
    public Component getComponent(Class<? extends Component> componentClass) {
        return null;
    }

    @Override
    public <T extends Component> void addComponent(T component) {

    }
}
