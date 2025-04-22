package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.ScrollUtil;

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
        Table table = new Table();
        table.top().padTop(20);
        table.defaults().center();
        table.add().expandX(); // helps make the table take full width
        table.center().row();

        // Add level selection buttons
        for (int i = 0; i <= Constants.LEVELS_COUNT; i++) {
            final int level = i;
            String buttonText = "Level " + i;
            if (i== 0){
                buttonText = "Tutorial";
            }

            if (i == GameFileHandler.inProgresslevelnumber()){
                table.row();
                TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(getSkin().get(TextButton.TextButtonStyle.class));
                customStyle.up = null;    // Remove the up state drawable
                customStyle.down = null;  // Optionally remove the down state drawable
                customStyle.over = null;  // Optionally remove the over state drawable
                
                LabelFactory.createLabel("(Continue)", getSkin(), "defaultFont", Color.BLACK, 0, table);

            } else {
                table.row();
            }
            ButtonFactory.createDefaultButton(buttonText, () -> game.setView(new GamePlayView(level)), table);

            table.row();
        }
        ScrollPane scrollPane = ScrollUtil.createStyledScrollPane(table);

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();
            scrollPane.setScrollY(0);
            scrollPane.updateVisualScroll();
        });

        // Main table setup
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        LabelFactory.createLabel("Select Level", getSkin(), "titleFont", Color.BLACK, TITLE_PADDING, mainTable);
        mainTable.top().add(scrollPane).expand().fill().row();
        mainTable.add(ButtonFactory.createButton("Back", getSkin(), game, () -> game.setView(new MainMenuView()),
            "customLoginStyle")).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(BUTTON_PADDING).padTop(20).row();
        stage.addActor(mainTable);
    }
}
