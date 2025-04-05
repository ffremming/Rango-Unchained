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

    //If you want to add animations, you simply add it here, add a playerstate in
    // animationscomponent and put the corresponding frames into the folder of the
    // corresponding animation to the player you want, for instance inside "Rango" directory
    public static void createPlayerAnimation(AnimationComponent animationComponent, String path) {
        animationComponent.putAnimation(AnimationComponent.PlayerState.LEFT, loadAnimationFromFolder(path + "/Left", 0.2f));
        animationComponent.putAnimation(AnimationComponent.PlayerState.RIGHT, loadAnimationFromFolder(path + "/Right", 0.2f));
        animationComponent.putAnimation(AnimationComponent.PlayerState.SHOOTING, loadAnimationFromFolder(path + "/Shoot", 0.2f));
        animationComponent.putAnimation(AnimationComponent.PlayerState.IDLE, loadAnimationFromFolder(path + "/Idle", 0.2f));
    }

    private static Animation<TextureRegion> loadAnimationFromFolder(String path, float frameDuration) {
        Array<TextureRegion> frames = new Array<>();

        FileHandle directory = Gdx.files.internal(path);
        for (FileHandle image : directory.list()) {
            System.out.println(image);
            Texture texture = new Texture(image);
            frames.add(new TextureRegion(texture));
        }
        return new Animation<TextureRegion>(frameDuration, frames);
    }

}
