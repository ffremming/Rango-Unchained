package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Firebase.MultiplayerManager;
import io.github.RangoUnchained.Model.Firebase.Utils.LobbyInfo;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;
import io.github.RangoUnchained.Views.Utils.SliderFactory;

public class PauseMenu extends Stage {
    private boolean isPaused = false;
    private final GameController game;
    private final int levelNumber;
    private final boolean isMultiplayer;
    private LobbyInfo lobby;

    public PauseMenu(GameController game, int levelNumber) {
        super(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        this.game = game;
        this.levelNumber = levelNumber;
        this.isMultiplayer = false;
        createUI();
    }

    // Constructor for Multiplayer
    public PauseMenu(GameController game, int levelNumber, LobbyInfo lobby) {
        super(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        this.game = game;
        this.levelNumber = levelNumber;
        this.lobby = lobby;
        this.isMultiplayer = true;
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        ButtonFactory.createButton("Back to game", 300, 60, GameController.getSkin(), game, this::togglePause, "customLoginStyle", table);
        if (!isMultiplayer) {
            ButtonFactory.createButton("Restart", 300, 60, GameController.getSkin(), game, this::restart, "customLoginStyle", table);
            if (levelNumber > 0) {
                ButtonFactory.createButton("Continue later", 300, 60, GameController.getSkin(), game, this::continueLater, "customLoginStyle", table);
            }
        }
        table.add(ButtonFactory.createButton("End game", GameController.getSkin(), game, this::endGame, "customLoginStyle")).center().padBottom(20);
        table.row();

        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().padBottom(20);

        Table volumeSlider = SliderFactory.createVolumeSlider(game, GameController.getSkin());

        // Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, GameController.getSkin());
        
        bottomTable.add(volumeSlider);

        Label SFXLabel = new Label("Volume sfx: ", GameController.getSkin());
        Slider SFXSlider = new Slider(0f, 1f, 0.01f, false, GameController.getSkin());
        SFXSlider.setValue(game.getSFXVolume());
        // SFXSlider.addListener(new ChangeListener() {
        //     @Override
        //     public void changed(ChangeEvent event, Actor actor) {
        //         float newVolume = SFXSlider.getValue();
        //         game.setSFXVolume(newVolume);
        //     }
        // });

        bottomTable.row();
        bottomTable.add(SFXLabel).pad(20);
        bottomTable.add(SFXSlider);

    }

    public void togglePause() {
        isPaused = !isPaused;
        if (!isPaused) {
            if (game.getScreen() instanceof BaseScreen) {
                Gdx.input.setInputProcessor(((BaseScreen) game.getScreen()).getStage());
            }
        } else {
            Gdx.input.setInputProcessor(this);
        }
    }

    private void restart() {
        Gdx.input.setInputProcessor(null);
        GameFileHandler.getInstance().resetCheckpointFile();
        isPaused = false;

        Gdx.app.postRunnable(() -> {
            game.setView(new GamePlayView(levelNumber));
        });
    }

    private void endGame() {
        if (isMultiplayer) {
            game.getMultiplayerManager().leaveLobby(lobby.lobbyId, game.getCurrentUser(), new MultiplayerManager.Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Gdx.app.postRunnable(() -> game.setView(new GameLobbyView()));
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("Error leaving lobby: " + e.getMessage());
                }
            });
        }
        game.setView(new GameOverView(levelNumber,false));
        GameFileHandler.getInstance().resetCheckpointFile();
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void continueLater() {
        // Save the game state and return to the main menu
        LevelController.getInstance().getLevel().checkpoint(3f);
        game.setView(new MainMenuView());
    }
}
