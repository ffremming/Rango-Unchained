package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.Gdx;

import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.BasicEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;

public class UtilFactory {

    public static Entity create(EntityData entityData) {
       
        if (entityData.typeInfo.type.equalsIgnoreCase("background")){
            return createBackground(entityData);
        }
        return null;
    }

    public static BasicEntity createBackground(EntityData entityData){
        SpriteComponent sprite = new SpriteComponent("Background/"+entityData.typeInfo.subType+".png",(int)(Gdx.graphics.getWidth()),(int)(Gdx.graphics.getHeight()));
        BasicEntity bg = new BasicEntity();
        bg.addComponent(sprite);
        return bg;
    }
    
}
