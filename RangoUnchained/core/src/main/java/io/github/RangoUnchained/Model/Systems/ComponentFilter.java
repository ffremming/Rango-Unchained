package io.github.RangoUnchained.Model.Systems;
import java.util.HashSet;
import java.util.Set;

import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Entities.Entity;

public class ComponentFilter {
    private final Set<Class<? extends Component>> requiredComponents = new HashSet<>();

    public ComponentFilter require(Class<? extends Component> componentClass) {
        requiredComponents.add(componentClass);
        return this; // Allow chaining
    }

    public boolean matches(Entity entity) {
        // Check if the entity has all required components
        for (Class<? extends Component> required : requiredComponents) {
            if (entity.getComponent(required) == null) {
                return false; // Missing a required component
            }
        }
        return true;
    }
}
