package io.github.RangoUnchained.Model.Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class AnimationComponent implements Component {

    public Map<PlayerState, Animation<TextureRegion>> animations = new HashMap<>();

    public PlayerState playerState = PlayerState.IDLE;
    public float framenumber;
    public void putAnimation(PlayerState playerState, Animation<TextureRegion> animation) {
        this.animations.put(playerState, animation);
    }

  public void setPlayerState(String state) {
        switch (state) {
            case "LEFT":
                playerState = PlayerState.LEFT;
                break;
            case "RIGHT":
                playerState = PlayerState.RIGHT;
                break;
            case "SHOOTING":
                playerState = PlayerState.SHOOTING;
                break;
            case "IDLE":
                playerState = PlayerState.IDLE;
                break;
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void increaseDelta(float delta) {
        framenumber += delta;
    }
    public float getFrame() {
        return framenumber;
    }

    public Animation<TextureRegion> getAnimation(PlayerState state) {
        return animations.get(state);
    }

    public enum PlayerState {
        IDLE,
        SHOOTING,
        LEFT,
        RIGHT

    }

}
