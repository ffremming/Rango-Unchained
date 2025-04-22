package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.Utils.ScoreInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.ScrollUtil;

public class ScoreboardView extends BaseScreen {
    public ScoreboardView() {
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

        // Add a loading message while fetching scores
        LabelFactory.createLabel("Loading scores...", getSkin(), "defaultFont", null, 300, table).center();

        // FirebaseManager is platform specific !
        FirebaseManager firebaseManager = GameController.getInstance().getFirebaseManager();

        // Fetch scores from Firebase asynchronously
        firebaseManager.loadScores(1, new FirebaseManager.Callback<List<ScoreInfo>>() {
            @Override
            public void onSuccess(final List<ScoreInfo> scores) {
                table.clear();

                if (scores.isEmpty()) {
                    LabelFactory.createLabel("No Scores found.", getSkin(), "defaultFont", null, 30, table).center();
                } else {
                    for (int i = 0; i < scores.size(); i++) {
                        ScoreInfo entry = scores.get(i);
                        String scoreText = (i + 1) + ". " + entry.displayName + ": " + entry.score + " points";
                        LabelFactory.createLabel(scoreText, getSkin(), "rioGrandeFont", Color.BLACK, 20, table).center();
                    }
                }
            }

            @Override
            public void onError(final Exception e) {
                table.clear();            

                LabelFactory.createLabel("Error loading scores: " + e.getMessage(), getSkin(), "defaultFont", Color.RED, 10, table).center();

                ButtonFactory.createDefaultButton("Back", () -> game.setView(new MainMenuView()), table);
            }
        });
        ScrollPane scrollPane = ScrollUtil.createStyledScrollPane(table);

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();
            scrollPane.setScrollY(0);
            scrollPane.updateVisualScroll();
        });

        // Main table setup
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        LabelFactory.createLabel("High Scores", getSkin(), "titleFont", Color.BLACK, TITLE_PADDING, mainTable);
        mainTable.top().add(scrollPane).expand().fill().row();
        ButtonFactory.createButton("Back", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_PADDING, getSkin(), game,
        () -> game.setView(new MainMenuView()), "customLoginStyle", mainTable);
        stage.addActor(mainTable);

    }
}
