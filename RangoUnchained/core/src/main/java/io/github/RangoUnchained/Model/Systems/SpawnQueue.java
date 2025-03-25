package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.EntityFactory;
import io.github.RangoUnchained.Model.Systems.SpawnRequest;

public class SpawnQueue {
    ArrayList<Entity> spawningEntities = new ArrayList<Entity>();

    public ArrayList<Entity> retrieveSpawningEntities() {
        ArrayList<Entity> temp = new ArrayList<Entity>(spawningEntities);
        spawningEntities.clear();
        return temp;
    }

    public void addSpawnRequest(float xPos, float yPos, int width, int width2, String name, Vector2 velocity, World world, int amount) {
        //method for adding new entities using factory - TODO should be generalised
        for (int i = 0; i < amount; i++) {
            Entity newBall = EntityFactory.createEntity(xPos, yPos, name, world, velocity);
            spawningEntities.add(newBall);
        }
    }
}
