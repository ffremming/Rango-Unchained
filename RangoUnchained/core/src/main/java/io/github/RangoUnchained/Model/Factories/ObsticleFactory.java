package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.Component;
import io.github.RangoUnchained.Model.Components.ContactComponent;
import io.github.RangoUnchained.Model.Components.SpriteComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.FloorEntity;
import io.github.RangoUnchained.Model.level.GameLevel.LevelData.EntityData;
import io.github.RangoUnchained.Views.Utils.Constants;

public class ObsticleFactory {

    /**
     * Creates an ObstacleEntity based on the supplied EntityData.
     */
    public static Entity create(EntityData entityData) {
        ObstacleEntity obstacle;

        String subType = entityData.typeInfo.subType;

        if (subType.toLowerCase().equals("floor")) {
            // Use the FloorEntity subtype for floor obstacles - used in contact handling
            obstacle = new FloorEntity();
        } else {
            obstacle = new ObstacleEntity();
        }
       
        obstacle.addComponent(createSpriteComponent(entityData));
        obstacle.addComponent(createContactComponent(entityData));
        BodyComponent body = createBodyComponent(entityData);
        body.getBody().setUserData(obstacle);
        obstacle.addComponent(body);

        return obstacle;
    }

    private static Component createContactComponent(EntityData entityData) {
        return new ContactComponent();
    }

    /**
     * Creates the BodyComponent for an obstacle based on its type and screen data.
     */
    private static BodyComponent createBodyComponent(EntityData entityData) {
        World world = LevelController.getInstance().getWorld();
        String name = entityData.typeInfo.type;
        int x = 0;
        int y = 0;
        float width = 0;
        float height = 0;
        BodyComponent body = null;
        String subType = entityData.typeInfo.subType;

        if (subType.equals("Left") || subType.equals("Right")) {
            // Use screen edges for left/right obstacles
            x = subType.equals("Right") ? Gdx.graphics.getWidth() : 0;
            y = 0;
            width = 32f / Constants.PPM;
            height = (2000f / Constants.PPM);
            body = BodyFactory.createBody(world, x, y, BodyDef.BodyType.StaticBody,
                    BodyFactory.createBoxFixture(width, height, EntityFactory.CATEGORY_OBSTACLE, EntityFactory.MASK_OBSTACLE), true);
        } else if (subType.equals("Roof") || subType.equals("Floor")) {
            // Roof and floor obstacles use horizontal placement
            if (subType.equals("Roof")) {
                y = Gdx.graphics.getHeight();
                height = 50f / Constants.PPM;
            } else {
                y = 50;
                height = 200f / Constants.PPM;
            }
            x = Gdx.graphics.getWidth() / 2;
            width = Gdx.graphics.getWidth() / ((float) Constants.PPM);
            body = BodyFactory.createBody(world, x, y, BodyDef.BodyType.StaticBody,
                    BodyFactory.createBoxFixture(width, height, EntityFactory.CATEGORY_OBSTACLE, EntityFactory.MASK_OBSTACLE), true);

        } else {
            // A generic obstacle defined by its given position in EntityData
            x = (int) entityData.dimension.x;
            y = (int) entityData.dimension.y;
            width = entityData.dimension.width / Constants.PPM;
            height =  entityData.dimension.height / Constants.PPM;
            body = BodyFactory.createBody(world, x, y, BodyDef.BodyType.StaticBody,
                    BodyFactory.createBoxFixture(width, height, EntityFactory.CATEGORY_OBSTACLE, EntityFactory.MASK_OBSTACLE), true);
        } 
        
        return body;
    }

    /**
     * Creates the SpriteComponent for an obstacle based on its type and computed dimensions.
     */
    private static SpriteComponent createSpriteComponent(EntityData entityData) {
        String name = entityData.typeInfo.type;
        float width = 0;
        float height = 0;
        String subType = entityData.typeInfo.subType;


        if (subType.equals("Left") || subType.equals("Right")) {
            width = 32f / Constants.PPM;
            height = (1000f / Constants.PPM) * 2;
        } else if (subType.equals("Roof") || subType.equals("Floor")) {
            if (subType.equals("Roof")) {
                height = 50f / Constants.PPM;
            } else {
                height = 200f / Constants.PPM;
            }
            width = Gdx.graphics.getWidth() / ((float) Constants.PPM);
        } else if (name.equals("Obsticle")) {
            width = entityData.dimension.width / Constants.PPM;
            height = entityData.dimension.height / Constants.PPM;
        }

        // Convert body dimensions back to pixels for sprite size
        int spriteWidth = (int) (width * Constants.PPM);
        int spriteHeight = (int) (height * Constants.PPM);

        SpriteComponent sprite;
        if (subType.equals("Floor")) {
            sprite = new SpriteComponent("Background/Floor.png", spriteWidth, spriteHeight);
        } else if (subType.equals("Roof")) {
            sprite = new SpriteComponent("Background/Roof.png", spriteWidth, spriteHeight);
        } else if (subType.equals("Left") || subType.equals("Right")) {
            sprite = new SpriteComponent("Background/Wall.png", spriteWidth, spriteHeight);
        } else {
            sprite = new SpriteComponent("Obstacles/"+entityData.typeInfo.subType+".png", spriteWidth, spriteHeight);
        }
        return sprite;
    }
}
