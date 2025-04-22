package io.github.RangoUnchained.Model.Factories;

import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;

public class EntityFactory {
    
    //categories
    public static final short CATEGORY_PLAYER     = 0x0001;
    public static final short CATEGORY_BALL       = 0x0002;
    public static final short CATEGORY_OBSTACLE   = 0x0004;
    public static final short CATEGORY_PROJECTILE = 0x0008;
    public static final short CATEGORY_POWERUP    = 0x0010; // New category for entities

    //masks for what should collide with what
    public static final short MASK_PLAYER     = CATEGORY_BALL | CATEGORY_OBSTACLE |CATEGORY_POWERUP ;
    public static final short MASK_BALL       = CATEGORY_PLAYER | CATEGORY_OBSTACLE | CATEGORY_PROJECTILE;
    public static final short MASK_OBSTACLE   = CATEGORY_PLAYER | CATEGORY_BALL |CATEGORY_POWERUP;
    public static final short MASK_PROJECTILE = CATEGORY_BALL;
    public static final short MASK_POWERUP    = CATEGORY_PLAYER | CATEGORY_OBSTACLE; // New mask for the new category

    public static Entity create(EntityData entityData,LevelData levelData){
        
        switch (entityData.typeInfo.type.toLowerCase()){
            case("ball"): 
                return BallFactory.create(entityData);
            case("player"):
                return PlayerFactory.create(entityData, levelData);
            case("powerup"):
                return PowerupFactory.create(entityData);
            case("projectile"):
                return ProjectileFactory.create(entityData);
            case("obsticle"):
                return ObsticleFactory.create(entityData);
            case("background"):
                return UtilFactory.create(entityData);
            default:
                return null;
        }
    }
}
