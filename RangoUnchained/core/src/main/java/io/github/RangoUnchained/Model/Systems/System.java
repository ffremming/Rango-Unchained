package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;

import io.github.RangoUnchained.Model.Entities.Entity;

public interface System {
    public static final ComponentFilter filter = new ComponentFilter();

    /**
     * Updates all entities that match the filter of the system
     * @param entities list of all entities
     */
    public default void update(ArrayList<Entity> entities){
        for(Entity entity : entities){
            if(filter.matches(entity)){
                updateEntity(entity);
            }
        }
    }

    /**method for updating entity in each system
     * this method should be implemented in each system
     * all entities that matches filter will be updated using this method
     * @param entity entity to be updated
     */
    abstract void updateEntity(Entity entity);
}
