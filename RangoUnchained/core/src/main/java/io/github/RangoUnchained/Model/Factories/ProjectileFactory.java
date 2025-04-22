package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.LifeTimeComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Components.TransformationComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Views.Utils.Constants;

public class ProjectileFactory {

    final static float HEIGHT = 96;
    final static float WIDTH = 16;

    public static Entity create(EntityData entityData) {
        ProjectileEntity projectile = new ProjectileEntity();

        projectile.addComponent(createLifetimeComponent(entityData));
        projectile.addComponent(createTransformationComponent(entityData));
        projectile.addComponent(createSpriteComponent(entityData));
        projectile.addComponent(createBodyComponent(entityData,projectile));
        projectile.addComponent(createContactComponent(entityData));


        return projectile;
    }


    private static Component createLifetimeComponent(EntityData entityData){
        return new LifeTimeComponent(50);
    }

    private static Component createTransformationComponent(EntityData entityData){
        TransformationComponent transComp = new TransformationComponent(3, 1, 1, 10
        ,TransformationComponent.RECTANGLE,
        TransformationComponent.UP
        ,true,
        5
        );
        transComp.setAlwaysReverse(false);
        return transComp;
    }

     private static Component createSpriteComponent(EntityData entityData){
        String path = entityData.typeInfo.type + "/"+ entityData.typeInfo.subType + ".png";
        SpriteComponent sprite = new SpriteComponent(path,WIDTH,HEIGHT);
        Gdx.app.log("sprite",sprite+",."+path);
        return sprite;
    }

    private static Component createBodyComponent(EntityData entityData,ProjectileEntity projectile){
        float width = (float)(WIDTH/ Constants.PPM);
        float height = (float)(HEIGHT/ Constants.PPM);
        Gdx.app.log("BodyComponent", "Width: " + width + ", Height: " + height);


        BodyComponent body = BodyFactory.createBody(LevelController.getInstance().getWorld(),
        entityData.dimension.x,
        entityData.dimension.y,
        BodyDef.BodyType.KinematicBody,
        BodyFactory.createBoxFixture(width, height, EntityFactory.CATEGORY_PROJECTILE, EntityFactory.MASK_PROJECTILE), true);
        body.getBody().setUserData(projectile);
        return body;
    }

    private static Component createContactComponent(EntityData entityData){
        return (new ContactComponent());

    }
}
