package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.github.RangoUnchained.Model.Components.AudioComponent;

public class AudioLoader {

    private static AudioLoader audioLoader;

    private HashMap<AudioComponent.ActionType, List<Sound>> audioMap = new HashMap<>();

    public static AudioLoader getInstance(){
        if (audioLoader == null) {
            audioLoader = new AudioLoader();
        }
        return audioLoader;
    }

    public List<Sound> getSounds(AudioComponent.ActionType actionType) {
        if (!audioMap.containsKey(actionType)) {
            loadAudioForAction(actionType);  // Load only when needed
        }
        return audioMap.get(actionType);
    }

    // Load audio files for a specific action type lazily
    private void loadAudioForAction(AudioComponent.ActionType actionType) {
        List<Sound> sounds = new LinkedList<>();

        switch (actionType) {
            case SHOOT:
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Lick_1.mp3")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Lick_2.mp3")));
                break;

            case MOVE:
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_1.wav")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_2.wav")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_3.wav")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_4.wav")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_5.wav")));
                break;

            case POP:
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_1.mp3")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_2.mp3")));
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_3.mp3")));
                break;

            case BOUNCE:
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/BalloonBounce.mp3")));
                break;

            default:
                throw new IllegalArgumentException("Unknown action type: " + actionType);
        }

        // Cache the loaded sounds for future use
        audioMap.put(actionType, sounds);
    }
}
