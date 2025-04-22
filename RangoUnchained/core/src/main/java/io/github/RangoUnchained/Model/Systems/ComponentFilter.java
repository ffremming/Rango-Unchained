package io.github.RangoUnchained.Model.Systems;
import java.util.HashSet;
import java.util.Set;

import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Entities.Entity;

/**
 * Utility class for filtering entities based on required components.
 */
public class ComponentFilter {
    private Set<Class<? extends Component>> requiredComponents = new HashSet<>();

    public ComponentFilter require(Class<? extends Component> componentClass) {
        requiredComponents.add(componentClass);
        return this;
    }

    public ComponentFilter ignore(Class<? extends Component> componentClass) {
        requiredComponents.add(componentClass);
        return this;
    }

    public boolean matches(Entity entity) {
        // Check if the entity has all required components
        for (Class<? extends Component> required : requiredComponents) {
            if (entity.getComponent(required) == null) {
                return false;
            }
        }
        return true;
    }
}
