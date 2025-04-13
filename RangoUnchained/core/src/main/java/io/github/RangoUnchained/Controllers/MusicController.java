package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.HashMap;

import com.badlogic.gdx.Screen;

import io.github.RangoUnchained.Views.GameOverView;
import io.github.RangoUnchained.Views.GamePlayView;
import io.github.RangoUnchained.Views.MainMenuView;

public class MusicController {

    private static MusicController instance;
    private Music activeSong;
    private float currentVolume = 0.2f;
    private HashMap<Class<? extends Screen>, Music> viewToMusic;

    private MusicController( ) {
        viewToMusic = new HashMap<>();
        initializeMusic();
    }

    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    private void initializeMusic(){
        Music music = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/rangoMusic.mp3"));
        viewToMusic.put(GamePlayView.class, music);
        music = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/rangoMusic.mp3"));
        viewToMusic.put(GameOverView.class, music);
        viewToMusic.put(MainMenuView.class, music);
    }

    protected void changeMusic(Screen view){
        Music newSong = viewToMusic.get(view.getClass());
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
