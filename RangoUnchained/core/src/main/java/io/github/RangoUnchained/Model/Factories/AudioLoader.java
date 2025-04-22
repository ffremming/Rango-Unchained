package io.github.RangoUnchained.Model.Factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.github.RangoUnchained.Controllers.AudioController;
import io.github.RangoUnchained.Model.Components.AudioComponent;

public class AudioLoader {

    private static AudioLoader audioLoader;

    private HashMap<AudioComponent.ActionType, List<Sound>> audioMap = new HashMap<>();
    private HashMap<AudioController.MusicKey, Music> keyToMusic = new HashMap<>();


    public static AudioLoader getInstance(){
        if (audioLoader == null) {
            audioLoader = new AudioLoader();
        }
        return audioLoader;
    }

    public void initializeMusic(){
        Music music = Gdx.audio.newMusic(Gdx.files.internal("Backgroundmusic/backgroundmusic_1.mp3"));
        keyToMusic.put(AudioController.MusicKey.GAMEPLAY, music);
        music = Gdx.audio.newMusic(Gdx.files.internal("Backgroundmusic/backgroundmusic_3.mp3"));
        keyToMusic.put(AudioController.MusicKey.DEFAULT, music);
    }

    public Music getMusic(AudioController.MusicKey key){
        return keyToMusic.get(key);
    }

    private AudioLoader(){
        InitializeAudio();
    }

    public List<Sound> getSounds(AudioComponent.ActionType actionType) {
        return audioMap.get(actionType);
    }

    private void InitializeAudio() {
        List<Sound> sounds = new LinkedList<>();
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Lick_1.mp3")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/Lick_2.mp3")));
        audioMap.put(AudioComponent.ActionType.SHOOT, sounds);


        sounds = new LinkedList<>();
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_1.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_2.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_3.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_4.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/walking_5.wav")));
        audioMap.put(AudioComponent.ActionType.MOVE, sounds);

        sounds = new LinkedList<>();
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_1.mp3")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_2.mp3")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/PopBalloonSound_3.mp3")));
        audioMap.put(AudioComponent.ActionType.POP, sounds);

        sounds = new LinkedList<>();
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("SoundEffects/BalloonBounce.mp3")));
        audioMap.put(AudioComponent.ActionType.BOUNCE, sounds);
    }

    public void dispose() {
        // dispose all sounds in audioMap
        for (List<Sound> sounds : audioMap.values()) {
            for (Sound sound : sounds) {
                sound.dispose();
            }
        }
        audioMap.clear();
    }

    public void disposeMusic() {
        // dispose all music in keyToMusic
        for (Music music : keyToMusic.values()) {
            music.dispose();
        }
        keyToMusic.clear();
    }
}
