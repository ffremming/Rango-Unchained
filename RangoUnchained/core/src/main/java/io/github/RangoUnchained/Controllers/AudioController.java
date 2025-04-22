package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.Screen;

import io.github.RangoUnchained.Model.Factories.AudioLoader;
import io.github.RangoUnchained.Model.Systems.AudioSystem;
import io.github.RangoUnchained.Views.Utils.BaseScreen;

public class AudioController {

    private static AudioController instance;
    private Music activeSong;
    private float currentVolume = 0.2f;
    private float sfxVolume = 0.5f;


    public enum MusicKey {
        DEFAULT,
        GAMEPLAY
    }

    private AudioController( ) {
        AudioLoader.getInstance().initializeMusic();
    }

    public static AudioController getInstance() {
        if (instance == null) {
            instance = new AudioController();
        }
        return instance;
    }

    protected void changeMusic(Screen view){
        MusicKey key = ((BaseScreen) view).getMusicKey();
        Music newSong = AudioLoader.getInstance().getMusic(key);

        if (activeSong != null && activeSong.equals(newSong) || newSong == null) {
            return;
        } if (activeSong != null) {
            activeSong.stop();
        }
        activeSong = newSong;
        playMusic();
    }

    public void changeVolume(float volume){
        currentVolume = volume;
        activeSong.setVolume(currentVolume);
    }

    public float getVolume(){
        return activeSong.getVolume();
    }


    public void changeSFXVolume(float volume){
        sfxVolume = volume;
        AudioSystem audioSystem = LevelController.getInstance().getAudioSystem();
        if (audioSystem != null){
            audioSystem.setVolume(sfxVolume);
        }
    }

    public float getSFXVolume(){
        return sfxVolume;
    }

    public void playMusic() {
        activeSong.play();
        activeSong.setVolume(currentVolume);
        activeSong.setLooping(true);
    }

    public void dispose(){
        if (activeSong != null){
            activeSong.dispose();
        }
        AudioLoader.getInstance().disposeMusic();
    }
}
