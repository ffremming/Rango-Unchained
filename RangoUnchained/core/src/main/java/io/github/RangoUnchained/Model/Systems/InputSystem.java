package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;

public class InputSystem implements Systems {

    private List<Entity> entities = new ArrayList<>();
    private boolean multiplayer;

    // Dette må endres så man ikke må lage en ny konstruktur og logikk hver gang man legger til player.
    // Kanskje når vi legger til spillere har parameter for hvilke knapper som skal være høyre og venstre?
    // Burde ha på samme måte som i andre systems en check når vi legger til entities i listen på om den har
    // riktige komponenter
    public InputSystem(PlayerEntity player1, PlayerEntity player2) {
        entities.add(player1);
        entities.add(player2);
        multiplayer = true;
    }

    public InputSystem(PlayerEntity player) {
        entities.add(player);
        multiplayer = false;
    }

    public InputSystem() {

    }
    // Method that can be called for controllers, delegates to correct handler based on gamemode
    public void handleInputs() {
        if (multiplayer) {
            handleInputMultiplayer();
        } else {
            handleInputSingleplayer();
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
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

    }


    @Override
    public void clearSystems() {
        entities.clear();
    }
}
