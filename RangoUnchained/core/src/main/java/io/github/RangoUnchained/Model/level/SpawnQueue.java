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

    public void addSpawnRequest(EntityData data, World world) {
        spawningEntities.add(data);
    }
}
