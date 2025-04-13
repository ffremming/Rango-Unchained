package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.HashMap;

import com.badlogic.gdx.Screen;
import io.github.RangoUnchained.Views.Utils.MusicProvider;

public class MusicController {

    private static MusicController instance;
    private Music activeSong;
    private float currentVolume = 0.2f;
    private HashMap<MusicKey, Music> keyToMusic;

    public enum MusicKey {
        DEFAULT,
        GAMEPLAY
    }

    private MusicController( ) {
        keyToMusic = new HashMap<>();
        initializeMusic();
    }

    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    private void initializeMusic(){
        Music music = Gdx.audio.newMusic(Gdx.files.internal("Backgroundmusic/backgroundmusic_1.mp3"));
        keyToMusic.put(MusicKey.GAMEPLAY, music);
        music = Gdx.audio.newMusic(Gdx.files.internal("Backgroundmusic/backgroundmusic_3.mp3"));
        keyToMusic.put(MusicKey.DEFAULT, music);
    }

    protected void changeMusic(Screen view){
        MusicKey key = ((MusicProvider) view).getMusicKey();
        Music newSong = keyToMusic.get(key);

        if (activeSong != null && activeSong.equals(newSong) || newSong == null) {
            return;
        } if (activeSong != null) {
            activeSong.dispose();
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

    public void playMusic() {
        activeSong.play();
        activeSong.setVolume(currentVolume);
        activeSong.setLooping(true);
    }
}
