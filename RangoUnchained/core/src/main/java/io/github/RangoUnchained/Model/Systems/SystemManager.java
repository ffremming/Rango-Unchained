package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Entities.Entity;

public class SystemManager {
    
    /**all systems */
    private ArrayList<Systems> systems = new ArrayList<>();
    private World world;

    public SystemManager() {
        initializeSystems();
    }
    
    /** method for updating all systems
     * systems should filter out entities that they can handle in their update method
     * @param entities list of all entities 
     * 
     */
    public void update(ArrayList<Entity> entities) {
        for (Systems system : systems) {
            system.update(entities);
        }
    }

    public void addSystem(Systems system){
        systems.add(system);
    }

    /**initializes all systems and adds them to system list */
    private void initializeSystems(){

        MovementSystem movementSystem = new MovementSystem();
        PhysicsSystem physicsSystem = new PhysicsSystem(-10);
        InputSystem inputSystem = new InputSystem();

        world = physicsSystem.getWorld();

        systems.add(movementSystem);
        systems.add(physicsSystem);
        systems.add(inputSystem);
    }

    /** method for getting world
     * is needed for the entityfactory
     */
    public World getWorld() {
        return world;
    }
}
