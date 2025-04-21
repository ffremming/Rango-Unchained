package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;
import io.github.RangoUnchained.Views.Utils.LabelFactory;

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

        // Table table = new Table();
        // // table.setFillParent(true);
        // table.top().padTop(50);
        // table.defaults().pad(10).center(); // default alignment for children

        Table table = new Table();
        table.top().padTop(50);
        table.defaults().padLeft(20).center();

        // Force table to use one column and center it horizontally
        table.add().expandX(); // helps make the table take full width

        // NEW: set table width to match parent when in ScrollPane
        // table.padLeft(20);

        // Center align content
        table.center().row();
        LabelFactory.createLabel("Select Level", getSkin(), "titleFont", null, 300, 60, 50, table);


        // Add level selection buttons
        for (int i = 0; i <= Constants.LEVELS_COUNT; i++) {
            final int level = i;
            String buttonText = "Level " + i;
            if (i== 0){
                buttonText = "Tutorial";
            }
            ButtonFactory.createButton(buttonText, 300, 60, getSkin(), game, () -> game.setView(new GamePlayView(level)), "customLoginStyle", table);

            if (i == GameFileHandler.inProgresslevelnumber()){
                table.row();
                TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(getSkin().get(TextButton.TextButtonStyle.class));
                customStyle.up = null;    // Remove the up state drawable
                customStyle.down = null;  // Optionally remove the down state drawable
                customStyle.over = null;  // Optionally remove the over state drawable

                TextButton continueButton = new TextButton("(continue)", customStyle);
                continueButton.getStyle().fontColor = getSkin().getColor("white");
                table.add(continueButton)
                .width(300)
                .height(60)
                .center()
                .padBottom(20);
            } else {
                table.row();
            }


            table.row();
        }

        // Back button to ScreenController Menu

         // 🔁 ScrollPane wraps your contentTable
        ScrollPane scrollPane = new ScrollPane(table, getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling, allow vertical

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();           // Force layout pass
            scrollPane.setScrollY(0);      // Set scroll to the top
            scrollPane.updateVisualScroll();
        });
        // 📦 Main table that fills the screen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().add(scrollPane).expand().fill().row();
        ButtonFactory.createDefaultButton("Back", () -> game.setView(new MainMenuView()), mainTable);


        stage.addActor(mainTable);
    }
}
