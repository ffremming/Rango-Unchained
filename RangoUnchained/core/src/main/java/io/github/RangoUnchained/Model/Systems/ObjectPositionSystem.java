package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.PositionComponent;
import io.github.RangoUnchained.Model.Components.VelocityComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class ObjectPositionSystem {

    private List<Entity> entities = new ArrayList<>();

    public void updateObjectPosition() {
        for (Entity e : entities) {
            // Usikker på hvor vi skal legge til collision detection. Kunne vært her også bare hatt en enkel
            // "if (object collide) {snu retning}", hvor vi sjekker posisjonen til komponenten og henter koordinater
            // til skjermen direkte her -> Gdx.Graphics...
            //
            PositionComponent positionComponent = (PositionComponent) e.getComponent(PositionComponent.class);
            VelocityComponent velocityComponent = (VelocityComponent) e.getComponent(VelocityComponent.class);

        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

}
