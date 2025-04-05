package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class SelectLevelView extends BaseScreen {

    public SelectLevelView() {
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
        Label titleLabel = new Label("Select Level", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        table.add(titleLabel).center().padBottom(50);
        table.row();
        
        // Add level selection buttons
        for (int i = 0; i <= 5; i++) {
            final int level = i;
            String buttonText = "Level " + i;
            if (i== 0){
                buttonText = "Tutorial";
            }
            table.add(ButtonFactory.createButton(buttonText, 300, 60, getSkin(), game, () -> game.setView(new GamePlayView(level)))).center().padTop(20);
            
            if (i == GameFileHandler.inProgresslevelnumber()){
                table.row();
                TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(getSkin().get(TextButton.TextButtonStyle.class));
                customStyle.up = null;    // Remove the up state drawable
                customStyle.down = null;  // Optionally remove the down state drawable
                customStyle.over = null;  // Optionally remove the over state drawable

                TextButton continueButton = new TextButton("(continue)", customStyle);
                continueButton.getStyle().fontColor = getSkin().getColor("white");
                table.add(continueButton).center().padBottom(0);
            } else {
                table.row();
            }


            table.row();
        }

        // Back button to ScreenController Menu
        table.add(ButtonFactory.createButton("Back", 300, 60, getSkin(), game, () -> game.setView(new MainMenuView()))).center().padTop(20);

        stage.addActor(table);
    }
}
