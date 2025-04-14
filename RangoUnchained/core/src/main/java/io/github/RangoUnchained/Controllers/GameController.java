
package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.RangoUnchained.Model.Factories.AnimationLoader;
import io.github.RangoUnchained.Model.Factories.AudioLoader;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;
import io.github.RangoUnchained.Model.Systems.AudioSystem;
import io.github.RangoUnchained.Views.MainMenuView;

public class GameController extends Game {

    private static GameController GameController;
    private FirebaseManager firebaseManager;
    private MultiplayerManager multiplayerManager;
    private static SpriteBatch batch;
    private static BitmapFont font;
    private static Skin skin;
    private Screen currentView;
    private Boolean isLoggedIn = false;
    private UserInfo currentUserInfo;

    private GameController() {
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        skin = new Skin(com.badlogic.gdx.Gdx.files.internal("skin/uiskin.json"));
        setView(new MainMenuView());
    }

    public static GameController getInstance(){
        if (GameController == null) {
            GameController = new GameController();
        }
        return GameController;
    }

    // Only done once on initialization
    public void setFirebaseManager(FirebaseManager firebaseManager) {
        this.firebaseManager = firebaseManager;
    }
    public FirebaseManager getFirebaseManager() {
        return firebaseManager;
    }

    public void setView(Screen view){
        if (currentView != null){
            currentView.dispose();
        }
        MusicController.getInstance().changeMusic(view);
        currentView = view;
        setScreen(currentView);
    }

    public void setMultiplayerManager(MultiplayerManager multiplayerManager) {
        this.multiplayerManager = multiplayerManager;
    }
    public MultiplayerManager getMultiplayerManager() {
        return multiplayerManager;
    }

    public static SpriteBatch getBatch() {
        if (batch == null) batch = new SpriteBatch();
        return batch;
    }

    public static BitmapFont getFont() {
        if (font == null) font = new BitmapFont();
        return font;
    }

    public static Skin getSkin() {
        return skin;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setUserInfo(UserInfo info) {
        this.currentUserInfo = info;
    }

    public UserInfo getCurrentUser() {
        return currentUserInfo;
    }

    public float getVolume() {return MusicController.getInstance().getVolume();}

    public void setVolume(float volume) {MusicController.getInstance().changeVolume(volume);}

    @Override
    public void dispose(){
        super.dispose();
        batch.dispose();
        skin.dispose();
        font.dispose();
        if (currentView != null){
            currentView.dispose();
        }
        AudioLoader.getInstance().dispose();
        MusicController.getInstance().dispose();
    }
}

