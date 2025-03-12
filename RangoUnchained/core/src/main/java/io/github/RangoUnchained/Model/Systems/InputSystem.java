package core.src.main.java.io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.List;

import core.src.main.java.io.github.RangoUnchained.Model.Components.InputComponent;
import core.src.main.java.io.github.RangoUnchained.Model.Entities.Entity;
import core.src.main.java.io.github.RangoUnchained.Model.Entities.PlayerEntity;

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

    public void handleInputSingleplayer() {
        InputComponent inputComponent = (InputComponent) entities.get(0).getComponent(InputComponent.class);
    }

    public void handleInputMultiplayer() {
        InputComponent p1_input = (InputComponent) entities.get(0).getComponent(InputComponent.class);
        InputComponent p2_input = (InputComponent) entities.get(1).getComponent(InputComponent.class);

        // if key pressed a,d or left,right change input to left/right = true/false. Set to false when not pressed
        // if key pressed q or p, stop make shoot true. shoot = true should be true for 0.2 seconds?
        // should stop when shooting or not? has to play the animation at least.
    }



}
