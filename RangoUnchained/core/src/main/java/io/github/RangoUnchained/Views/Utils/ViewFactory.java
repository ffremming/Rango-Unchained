package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.Screen;

import io.github.RangoUnchained.Views.GameOverView;
import io.github.RangoUnchained.Views.GamePlayView;
import io.github.RangoUnchained.Views.MainMenuView;
import io.github.RangoUnchained.Views.ScoreboardView;
import io.github.RangoUnchained.Views.SelectLevelView;

public class ViewFactory {
    public static Screen createView(String type) {
        switch (type) {
            case "MAIN_MENU":
                return new MainMenuView();
            case "GAMEPLAY":
                return new GamePlayView();
            case "SCORE_BOARD":
                return new ScoreboardView();
            case "LEVEL_SCREEN":
                return new SelectLevelView();
            case "GAME_OVER":
                return new GameOverView();
            default:
                throw new IllegalArgumentException("Unknown screen type: " + type);
        }
    }
}
