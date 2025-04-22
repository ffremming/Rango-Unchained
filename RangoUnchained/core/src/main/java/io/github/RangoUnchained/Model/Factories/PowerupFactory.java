package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.physics.box2d.BodyDef;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.PowerUpEntity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Views.Utils.Constants;

public class PowerupFactory {

    final static int SIZE = 64;

    public static PowerUpEntity create(EntityData entityData) {
        PowerUpEntity powerUp = new PowerUpEntity();
        powerUp.addComponent(createSpriteComponent(entityData));
        powerUp.addComponent(createBodyComponent(entityData,powerUp));
        powerUp.addComponent(createPowerUpComponent(entityData));
        powerUp.addComponent(createContactComponent(entityData));
        powerUp.addComponent(createSpeedComponent(entityData));
        
        return powerUp;
    }

    private static Component createSpriteComponent(EntityData entityData){
        String path = entityData.typeInfo.type + "/"+ entityData.typeInfo.subType + ".png";
        SpriteComponent sprite = new SpriteComponent(path,SIZE,SIZE);
        return sprite;
    }

    private static Component createBodyComponent(EntityData entityData,PowerUpEntity powerup){
        float width = (SIZE/ Constants.PPM);
        float height = (SIZE/ Constants.PPM);

        BodyComponent body = BodyFactory.createBody(LevelController.getInstance().getWorld(),
        entityData.dimension.x,
        entityData.dimension.y,
        BodyDef.BodyType.DynamicBody, 
        BodyFactory.createNoxBounceBoxFixture(width, height, EntityFactory.CATEGORY_POWERUP, EntityFactory.MASK_POWERUP), true);
        body.getBody().setLinearVelocity(entityData.velocity);
        body.getBody().setUserData(powerup);
        return body;
    }

    private static Component createPowerUpComponent(EntityData entityData){
        int powerupType = entityData.typeInfo.subType.equalsIgnoreCase("speed") ? PowerUpComponent.SPEED :
        entityData.typeInfo.subType.equalsIgnoreCase("shield") ? PowerUpComponent.SHIELD :
        PowerUpComponent.HEALTH;
        PowerUpComponent powerUpComponent = new PowerUpComponent(powerupType);
        return powerUpComponent;
    }

    private static Component createContactComponent(EntityData entityData){
        return new ContactComponent();
    }

    private static Component createSpeedComponent(EntityData entityData){
        return  new SpeedComponent(5f);
    }
}
