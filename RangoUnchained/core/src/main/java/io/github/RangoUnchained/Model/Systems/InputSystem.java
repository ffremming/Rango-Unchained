package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;


import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class InputSystem implements System {

    private Touchpad touchpad;

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
    }

    public Object handleShoot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleShoot'");
    }
}
