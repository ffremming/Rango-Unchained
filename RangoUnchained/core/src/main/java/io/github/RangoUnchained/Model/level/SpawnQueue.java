package io.github.RangoUnchained.Model.level;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.Dimension;

public class SpawnQueue {
    ArrayList<EntityData> spawningEntities = new ArrayList<EntityData>();

    
    public ArrayList<EntityData> retrieveSpawningEntities() {
        ArrayList<EntityData> temp = new ArrayList<EntityData>(spawningEntities);
        spawningEntities.clear();
        return temp;
    }

    public void addSpawnRequest(float xPos, float yPos, int width, int width2, String name, Vector2 velocity, World world) {
        //method for adding new entities using factory - TODO should be generalised
        
            GameLevel.LevelData.EntityData data = new EntityData();
            data.name = name;
            data.dimension = new Dimension();
            data.dimension.x = xPos;
            data.dimension.y = yPos;
            data.velocity = velocity;

            spawningEntities.add(data);
    }
}
