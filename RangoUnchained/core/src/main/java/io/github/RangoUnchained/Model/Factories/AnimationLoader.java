package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import io.github.RangoUnchained.Model.Components.AnimationComponent;

public class AnimationLoader {

    // If you want to add animations:
    // Add a playerstate that plays the animation into enum in animationscomponent
    // Put frames into the folder of the playertype and animationtype, for instance "Rango/Left"
    // Add call to putAnimation for the playerstate and path of animationType.
    public static void createPlayerAnimation(AnimationComponent animationComponent, String path) {
        animationComponent.putAnimation(AnimationComponent.PlayerState.LEFT, loadAnimationFromFolder(path + "/Left", 0.2f));
        animationComponent.putAnimation(AnimationComponent.PlayerState.RIGHT, loadAnimationFromFolder(path + "/Right", 0.2f));
        animationComponent.putAnimation(AnimationComponent.PlayerState.SHOOTING, loadAnimationFromFolder(path + "/Shoot", 0.2f));
        animationComponent.putAnimation(AnimationComponent.PlayerState.IDLE, loadAnimationFromFolder(path + "/Idle", 0.2f));
    }

    private static Animation<TextureRegion> loadAnimationFromFolder(String path, float frameDuration) {

        Array<TextureRegion> frames = new Array<>();

        FileHandle image = Gdx.files.local("assets/assets.txt");
        for (String asset : image.readString().split("\n")) {
            if (asset.startsWith(path)) {
                Texture texture = new Texture(Gdx.files.internal(asset.trim()));
                frames.add(new TextureRegion(texture));
            }
        }

        return new Animation<TextureRegion>(frameDuration, frames);
    }

}
