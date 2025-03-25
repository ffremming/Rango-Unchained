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
        if (!p1_input.isLocked()){
            handleInput(p1_input, entity);
        } else {
            p1_input.decrementInputLock();
        }
    }

    private synchronized void handleInput(InputComponent p1_input, Entity entity) {

        p1_input.setShoot(false);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !p1_input.isShoot()){
            p1_input.setShoot(true);
            handleShoot(entity);
            p1_input.setLeft(false);
            p1_input.setRight(false);
            p1_input.setTimer(100);

        } else {
            p1_input.setLeft(Gdx.input.isKeyPressed(Input.Keys.A));
            p1_input.setRight(Gdx.input.isKeyPressed(Input.Keys.D));
        }
        
        if (Gdx.input.isTouched()){
            p1_input.setLeft(touchpad.getKnobPercentX() < 0);
            p1_input.setRight(0.5 < touchpad.getKnobPercentX());
        }
    }

    public synchronized void handleShoot(ArrayList<Entity> entities) {
        
        for(Entity entity : entities){
            if(filter.matches(entity)){
                handleShoot(entity);
            }
        }
    }

    private void handleShoot(Entity entity){
        InputComponent p1_input = (InputComponent) entity.getComponent(InputComponent.class);
        if (!p1_input.isLocked()){

            filter.require(BodyComponent.class);
            if(filter.matches(entity)){
                Body body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();
                LevelController.getInstance().handleSpawnRequests(body.getPosition().x, body.getPosition().y +42,0,0,"Projectile",new Vector2(0,0),1);
            }
            filter.ignore(BodyComponent.class);
        }
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
