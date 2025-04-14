package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class MainMenuView extends BaseScreen {

    public MainMenuView() {
        super(GameController.getInstance());
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label titleLabel = new Label("Rango Unchained", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        table.add(titleLabel).center().padBottom(30);
        table.row();

        // Display login state
        String userState = game.getIsLoggedIn() ? "Play" : "Play as guest";

        // Add buttons
        if (!game.getIsLoggedIn()){
            table.add(ButtonFactory.createButton("Log In", 300, 60, getSkin(), game,  () -> game.setView(new LoginView()))).center().padBottom(20);
            table.row();
        }

        table.add(ButtonFactory.createButton(userState, 300, 60, getSkin(), game,
            () -> game.setView(new SelectLevelView()))).center().padBottom(20);
        table.row();
        table.add(ButtonFactory.createButton("Scoreboard", 300, 60, getSkin(), game,
            () -> game.setView(new ScoreboardView()))).center().padBottom(20);
        table.row();
        // Add extra options for logged in users
        if (game.getIsLoggedIn()) {
            table.add(ButtonFactory.createButton("Multiplayer", 300, 60, getSkin(), game,
                () -> game.setView(new GameLobbyView()))).center().padBottom(50);
            table.row();
            // Display logged in user
            table.add(new Label("Logged in as: " + game.getCurrentUser().getDisplayName(), labelStyle)).padBottom(20);
            table.row();
            table.add(ButtonFactory.createButton("Change username", 300, 60, getSkin(), game,
                () -> game.setView(new CreateUsernameView()))).center().padBottom(20);
            table.row();
            table.add(ButtonFactory.createButton("Log Out", 300, 60, getSkin(), game, () -> {
                game.getFirebaseManager().signOut();
                game.setIsLoggedIn(false);
                game.setView(new MainMenuView());
            })).center();
        }

        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().padBottom(20);

        Label volumeLabel = new Label("Volume: ", labelStyle);
        volumeLabel.setFontScale(1.3f);
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, getSkin());

        volumeSlider.setValue(game.getMusicVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = volumeSlider.getValue();
                game.setMusicVolume(newVolume);
            }
        });

        bottomTable.add(volumeLabel);
        bottomTable.add(volumeSlider);

        Label SFXLabel = new Label("Volume sfx: ", labelStyle);
        SFXLabel.setFontScale(1.3f);
        Slider SFXSlider = new Slider(0f, 1f, 0.01f, false, getSkin());

        SFXSlider.setValue(game.getSFXVolume());
        SFXSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = SFXSlider.getValue();
                game.setSFXVolume(newVolume);
            }
        });

        bottomTable.row();
        bottomTable.add(SFXLabel).pad(20);
        bottomTable.add(SFXSlider);

        // Add table to stage
        stage.addActor(table);
        stage.addActor(bottomTable);

    }

}
