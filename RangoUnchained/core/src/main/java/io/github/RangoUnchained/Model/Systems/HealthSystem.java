package io.github.RangoUnchained.Model.Systems;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategy;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;

public class HealthSystem implements System, ContactStrategy{

    private ComponentFilter filter = new ComponentFilter();

    public HealthSystem() {
        filter
        .require(HealthComponent.class);
    }

    @Override
    public void updateEntity(Entity entity) {
        //nothing yet?
        if (!((HealthComponent)entity.getComponent(HealthComponent.class)).isAlive()){
            kill(entity);
        }
    }

    private void kill(Entity entity) {
        LevelController.getInstance().handleRemovalRequests(entity);
        //some other logic TODO
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }

    @Override
    public void setContactStrategies() {
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);
        centralContactListener.subscribe(
        BallEntity.class, PlayerEntity.class,
        this::decreaseHealth, // For beginContact
        null);
    }

    /**contact strategy between playerEntity and Ballentity */
    private void decreaseHealth(CollisionEvent collisionEvent){
        PlayerEntity player;

        if (collisionEvent.entityA instanceof PlayerEntity) {
            player = (PlayerEntity) collisionEvent.entityA;;
        } else {
            player = (PlayerEntity) collisionEvent.entityB;
        }
        HealthComponent healthComponent =  ((HealthComponent)player.getComponent(HealthComponent.class));

        if (healthComponent.isShieldActive()) {
            //healthComponent.setShieldActive(false);
        } else {
            healthComponent.decreaseHealth();
            LevelController.getInstance().getSystem(TutorialSystem.class).flagPlayerHit();
        }
    }





}
