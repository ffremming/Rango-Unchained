package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.AnimationComponent;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.HealthComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.PowerUpComponent;
import io.github.RangoUnchained.Model.Components.SpeedComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.TutorialComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData.TypeInfo;
import io.github.RangoUnchained.Views.Utils.Constants;

public class PlayerFactory {

    public static Entity create(EntityData entityData, LevelData levelData) {
        PlayerEntity player = new PlayerEntity();

        player.addComponent(createBodyComponent(entityData,player));
        player.addComponent(createHealthComponent(entityData));
        player.addComponent(createSpriteComponent(entityData));
        player.addComponent(createContactComponent(entityData));
        player.addComponent(createInputComponent(entityData));
        player.addComponent(createAnimationComponent(entityData));
        player.addComponent(createSpeedComponent(entityData));
        player.addComponent(createPowerUpComponent(entityData));

        if (levelData.metaData.levelnr == 0){
            player.addComponent(createTutorialComponent(entityData));
        }
        return player;
    }

    private static Component createPowerUpComponent(EntityData entityData) {
        return new PowerUpComponent(-1);
    }

    private static Component createSpeedComponent(EntityData entityData) {
        return new SpeedComponent(5f);
    }

    private static Component createAnimationComponent(EntityData entityData) {
        AnimationComponent animation = new AnimationComponent();
        AnimationLoader.createPlayerAnimation(animation, entityData.typeInfo.subType);
        return animation;
    }

    private static Component createInputComponent(EntityData entityData) {
        return new InputComponent();
    }

    private static Component createTutorialComponent(EntityData entityData){
        return new TutorialComponent();
    }
     
    private static Component createBodyComponent(EntityData entityData, PlayerEntity player){
        float width = 86 / Constants.PPM;
        float height = 128 / Constants.PPM;
        BodyComponent body = BodyFactory.createBody(LevelController.getInstance().getWorld(), entityData.dimension.x, entityData.dimension.y, BodyDef.BodyType.DynamicBody, BodyFactory.createNoxBounceBoxFixture(width, height,EntityFactory.CATEGORY_PLAYER,EntityFactory.MASK_PLAYER),true);
        body.getBody().setUserData(player);
        return body;
    }

    private static Component createSpriteComponent(EntityData entityData){
        TypeInfo typeInfo = entityData.typeInfo;
        AnimationComponent animation = (AnimationComponent)createAnimationComponent(entityData);
        TextureRegion firstFrame = animation.getAnimation(animation.getPlayerState()).getKeyFrame(0);
        SpriteComponent sprite = new SpriteComponent(firstFrame, 86, 128,typeInfo.type);
        return sprite;
    }

    private static Component createContactComponent(EntityData entityData){
        ContactComponent contactComponent = new ContactComponent();
        return contactComponent;
    }

    private static Component createHealthComponent(EntityData entityData){
        HealthComponent health = entityData.health <= 0 ? new HealthComponent(8) : new HealthComponent(entityData.health);
        return health;
    }
}
