package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
public class GameOverView extends BaseScreen {

    public GameOverView() {
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
        Label titleLabel = new Label("Game Over", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(titleLabel).center().padBottom(50);
        table.row();

        // Retry button (goes back to level selection)
        table.add(ButtonFactory.createButton("Play", 300, 60, getSkin(),  game, () -> game.setView(new SelectLevelView()))).center().padBottom(20);
        table.row();

        // Back to main menu button
        table.add(ButtonFactory.createButton("Main Menu", 300, 60, getSkin(), game,() -> game.setView(new MainMenuView()))).center();

        stage.addActor(table);
    }
}
