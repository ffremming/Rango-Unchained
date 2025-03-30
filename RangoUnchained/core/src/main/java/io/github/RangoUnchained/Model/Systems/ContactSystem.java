package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.physics.box2d.*;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ContactSystem implements ContactListener, System {

    private ComponentFilter filter = new ComponentFilter();


    public static class CollisionEvent {
        public final Entity entityA;
        public final Entity entityB;
        public final Contact contact;
        public CollisionEvent(Entity entityA, Entity entityB, Contact contact) {
            this.entityA = entityA;
            this.entityB = entityB;
            this.contact = contact;
        }
    }

    private static class Subscription {
        final Class<?> typeA;
        final Class<?> typeB;
        final Consumer<CollisionEvent> beginAction;
        final Consumer<CollisionEvent> endAction;

        Subscription(Class<?> typeA, Class<?> typeB,
                     Consumer<CollisionEvent> beginAction,
                     Consumer<CollisionEvent> endAction) {
            this.typeA = typeA;
            this.typeB = typeB;
            this.beginAction = beginAction;
            this.endAction = endAction;
        }
    }

    private final List<Subscription> subscriptions = new ArrayList<>();

    public void subscribe(Class<?> typeA, Class<?> typeB,
                          Consumer<CollisionEvent> beginAction,
                          Consumer<CollisionEvent> endAction) {
        subscriptions.add(new Subscription(typeA, typeB, beginAction, endAction));
    }

    public ContactSystem(World world){
        world.setContactListener(this);

        filter
        .require(ContactComponent.class);
    }

    // Helper method that extracts entities and dispatches the event.
    private void dispatchEvent(Contact contact, BiConsumer<Subscription, CollisionEvent> handler) {
        Object dataA = contact.getFixtureA().getBody().getUserData();
        Object dataB = contact.getFixtureB().getBody().getUserData();
        if (!(dataA instanceof Entity) || !(dataB instanceof Entity)) return;
        Entity eA = (Entity) dataA;
        Entity eB = (Entity) dataB;

        if (((ContactComponent)(eA.getComponent(ContactComponent.class))).isContactLocked()){return;}
        if (((ContactComponent)(eB.getComponent(ContactComponent.class))).isContactLocked()){return;}

        CollisionEvent event = new CollisionEvent(eA, eB, contact);

        for (Subscription sub : subscriptions) {
            if ((sub.typeA.isInstance(eA) && sub.typeB.isInstance(eB)) ||
                (sub.typeA.isInstance(eB) && sub.typeB.isInstance(eA))) {
                handler.accept(sub, event);
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        dispatchEvent(contact, (sub, event) -> {
            if (sub.beginAction != null) {
                sub.beginAction.accept(event);
            }
        });
    }

    @Override
    public void endContact(Contact contact) {
        dispatchEvent(contact, (sub, event) -> {
            if (sub.endAction != null) {
                sub.endAction.accept(event);
            }
        });
    }

    @Override public void preSolve(Contact contact, Manifold oldManifold) { }
    @Override public void postSolve(Contact contact, ContactImpulse impulse) { }

    @Override
    public void updateEntity(Entity entity) {
        ((ContactComponent)(entity.getComponent(ContactComponent.class))).decrementContactLock();
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
