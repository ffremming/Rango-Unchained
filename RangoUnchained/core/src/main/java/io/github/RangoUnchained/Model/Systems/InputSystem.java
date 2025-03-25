package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Components.BodyComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class InputSystem implements System {

    private Touchpad touchpad;
    private ComponentFilter filter = new ComponentFilter();

    public InputSystem() {        
        filter
        .require(InputComponent.class);
    }

    public void setTouchpad(Touchpad touchpad){
        this.touchpad = touchpad;
    }

    @Override
    public void updateEntity(Entity entity) {
       
        InputComponent p1_input = (InputComponent) entity.getComponent(InputComponent.class);

        p1_input.setLeft(Gdx.input.isKeyPressed(Input.Keys.A));
        p1_input.setRight(Gdx.input.isKeyPressed(Input.Keys.D));
        p1_input.setShoot(Gdx.input.isKeyPressed(Input.Keys.SPACE));

        if (Gdx.input.isTouched()){
            p1_input.setLeft(touchpad.getKnobPercentX() < 0);
            p1_input.setRight(0.5 < touchpad.getKnobPercentX());
        }
        if (p1_input.isShoot()){
            handleShoot(entity);
        }
    }

    public void handleShoot(ArrayList<Entity> entities) {

        for(Entity entity : entities){
            if(filter.matches(entity)){
                handleShoot(entity);
            }
        }
    }

    private void handleShoot(Entity entity){
        Gdx.app.log("input","shoot"+ entity.getClass().getName());

        if(filter.matches(entity)){
            Body body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();
            LevelController.getInstance().handleSpawnRequests(body.getPosition().x+7, body.getPosition().y + 160,0,0,"Projectile",new Vector2(0,0),1);
        }
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
