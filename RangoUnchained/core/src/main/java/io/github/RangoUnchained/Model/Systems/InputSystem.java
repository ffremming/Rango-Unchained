package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class InputSystem {

    private List<Entity> entities = new ArrayList<>();
    private boolean multiplayer;

    public InputSystem(PlayerEntity player1, PlayerEntity player2) {
        entities.add(player1);
        entities.add(player2);
        multiplayer = true;
    }

    public InputSystem(PlayerEntity player) {
        entities.add(player);
        multiplayer = false;
    }

    // Method that can be called for controllers, delegates to correct handler based on gamemode
    public void handleInputs() {
        if (multiplayer) {
            handleInputMultiplayer();
        } else {
            handleInputSingleplayer();
        }
    }

    // Updates the input components of the player in singleplayer
    public void handleInputSingleplayer() {
        InputComponent p1_input = (InputComponent) entities.get(0).getComponent(InputComponent.class);

        p1_input.setLeft(Gdx.input.isKeyPressed(Input.Keys.A));
        p1_input.setRight(Gdx.input.isKeyPressed(Input.Keys.D));
        p1_input.setShoot(Gdx.input.isKeyPressed(Input.Keys.SPACE));

    }

    // Updates the input components of the players in multiplayer
    public void handleInputMultiplayer() {
        InputComponent p1_input = (InputComponent) entities.get(0).getComponent(InputComponent.class);
        InputComponent p2_input = (InputComponent) entities.get(1).getComponent(InputComponent.class);

        // if key pressed a,d or left,right change input to left/right = true/false. Set to false when not pressed
        // if key pressed q or p, stop make shoot true. shoot = true should be true for 0.2 seconds?
        // should stop when shooting or not? has to play the animation at least.

        p1_input.setLeft(Gdx.input.isKeyPressed(Input.Keys.A));
        p1_input.setRight(Gdx.input.isKeyPressed(Input.Keys.D));
        p1_input.setShoot(Gdx.input.isKeyPressed(Input.Keys.SPACE));

        p2_input.setLeft(Gdx.input.isKeyPressed(Input.Keys.LEFT));
        p2_input.setRight(Gdx.input.isKeyPressed(Input.Keys.RIGHT));
        p2_input.setShoot(Gdx.input.isKeyPressed(Input.Keys.P));
    }



}
