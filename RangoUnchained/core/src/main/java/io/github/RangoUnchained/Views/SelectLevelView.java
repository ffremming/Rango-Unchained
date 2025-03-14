package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.RangoUnchained.Main;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class SelectLevelView extends BaseScreen {

    public SelectLevelView(Main game) {
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
        Label titleLabel = new Label("Select Level", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        table.add(titleLabel).center().padBottom(50);
        table.row();

        // Add level selection buttons
        for (int i = 1; i <= 3; i++) {
            table.add(ButtonFactory.createButton("Level " + i, 300, 60, getSkin(), game, new GameView(game, i))).center().padBottom(20);
            table.row();
        }

        // Back button to Main Menu
        table.add(ButtonFactory.createButton("Back", 300, 60, getSkin(), game, new MainMenuView(game))).center().padTop(20);

        stage.addActor(table);
    }
}
