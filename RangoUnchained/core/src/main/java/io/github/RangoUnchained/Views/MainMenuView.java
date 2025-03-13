package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.RangoUnchained.Main;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class MainMenuView extends BaseScreen {

    public MainMenuView(Main game) {
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
        Label titleLabel = new Label("Rango Unchained", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        table.add(titleLabel).center().padBottom(50);
        table.row();

        // Add buttons
        table.add(ButtonFactory.createButton("Game Level Selection", 300, 60, getSkin(), game, new SelectLevelView(game))).center().padBottom(20);
        table.row();
        table.add(ButtonFactory.createButton("Scoreboard", 300, 60, getSkin(), game, ScoreboardView.getInstance(game))).center();

        // Add table to stage
        stage.addActor(table);
    }
}
