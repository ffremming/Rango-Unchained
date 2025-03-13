package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.RangoUnchained.Main;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class ScoreboardView extends BaseScreen {

    private static ScoreboardView instance;

    public static ScoreboardView getInstance(Main game) {
        if (instance == null) {
            instance = new ScoreboardView(game);
        }
        return instance;
    }

    private ScoreboardView(Main game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label titleLabel = new Label("High Scores", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        table.add(titleLabel).center().padBottom(50);
        table.row();

        // Example Score List
        for (int i = 1; i <= 5; i++) {
            table.add(new Label("Player " + i + ": " + (1000 - i * 100) + " pts", labelStyle)).center().padBottom(10);
            table.row();
        }

        // Back button to Main Menu
        table.add(ButtonFactory.createButton("Back", 300, 60, getSkin(), game, new MainMenuView(game))).center().padTop(20);

        stage.addActor(table);
    }
}
