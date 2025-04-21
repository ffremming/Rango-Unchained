package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Entities.Entity;


public class SystemManager {

    /**all systems */
    private ArrayList<System> systems = new ArrayList<>();
    private World world;

    public SystemManager() {
        initializeSystems();
    }

    /**
     * Returns a system of the specified class type.
     *
     * @param <T> the type of the system
     * @param systemClass the class of the system to return
     * @return the system of the specified class type, or null if not found
     */
    public <T extends System> T getSystem(Class<T> systemClass) {
        for (System system : systems) {
            if (systemClass.isInstance(system)) {
                return systemClass.cast(system);
            }
        }
        return null;
    }

    /** method for updating all systems
     * systems should filter out entities that they can handle in their update method
     * @param entities list of all entities
     *
     */
    public void update(ArrayList<Entity> entities, float delta) {
        for (System system : systems) {
            system.update(entities, delta);
        }
    }

    /**initializes all systems and adds them to system list */
    private void initializeSystems(){

        TransformationSystem transformationSystem = new TransformationSystem();
        MovementSystem movementSystem = new MovementSystem();
        PhysicsSystem physicsSystem = new PhysicsSystem();
        InputSystem inputSystem = new InputSystem();
        LifeTimeSystem lifeTimeSystem = new LifeTimeSystem();
        ContactSystem contactSystem = new ContactSystem(physicsSystem.getWorld());
        HealthSystem healthSystem = new HealthSystem();
        TutorialSystem tutorialSystem = new TutorialSystem();
        PowerUpSystem powerUpSystem = new PowerUpSystem();
        AudioSystem audioSystem = new AudioSystem(contactSystem, GameController.getInstance().getSFXVolume());
        AnimationSystem animationSystem = new AnimationSystem();

        world = physicsSystem.getWorld();

        systems.add(movementSystem);
        systems.add(physicsSystem);
        systems.add(inputSystem);
        systems.add(animationSystem);
        systems.add(audioSystem);

        systems.add(lifeTimeSystem);
        systems.add(transformationSystem);
        systems.add(contactSystem);
        systems.add(healthSystem);
        systems.add(powerUpSystem);
        systems.add(tutorialSystem);
    }

    /** method for getting world
     * is needed for the entityfactory
     */
    public World getWorld() {
        PhysicsSystem physicsSystem = getSystem(PhysicsSystem.class);
        return physicsSystem != null ? physicsSystem.getWorld() : null;
    }

    public void dispose() {
        if (systems != null) {
            systems.clear();
            systems = null;
        }
        if (world != null) {
            world.dispose();
            world = null;
        }
    }
}
