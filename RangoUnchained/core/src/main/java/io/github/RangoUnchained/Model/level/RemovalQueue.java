package io.github.RangoUnchained.Model.level;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Entities.Entity;

public class RemovalQueue {
    ArrayList<Entity> removalQueue = new ArrayList<Entity>();

    public ArrayList<Entity> getRemovalEntities(World world){
        ArrayList<Entity> removalQueueCopy = new ArrayList<Entity>(removalQueue);
        removalQueue.clear();
        return removalQueueCopy;
    }

    public void addRemovalRequest(Entity entity){
        removalQueue.add(entity);
    }
}
