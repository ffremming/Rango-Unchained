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
    private HashMap<Screen, Music> viewToMusic;

    private MusicController( ) {
        viewToMusic = new HashMap<>();
    }

    // Public accessor for the singleton instance, with lazy initialization
    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    protected void setMusic(Screen view) {
        if (!viewToMusic.containsKey(view)){
            selectMusic(view);
            return;
        }
        changeMusic(viewToMusic.get(view));
    }

    private void selectMusic(Screen view){
        Music music;
        if (view instanceof GamePlayView) {
            music = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/rangoMusic.mp3"));
        }else if (view instanceof MainMenuView) {
            music = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/rangoMusic.mp3"));
        }else if (view instanceof GameOverView) {
            music = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/rangoMusic.mp3"));
        }else{
            return;
        }
        viewToMusic.put(view, music);
        changeMusic(music);
    }

    private void changeMusic(Music newSong){
        if(!(activeSong == null)){
            stopMusic();
        }
        activeSong = newSong;
        activeSong.play();
    }

    public void changeVolume(int volume){
        activeSong.setVolume(volume);
    }

    public void pauseMusic() {
        activeSong.pause();
    }

    public void playMusic() {
        activeSong.pause();
    }

    private void stopMusic() {
        activeSong.dispose();
    }

}
