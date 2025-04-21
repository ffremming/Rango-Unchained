package io.github.RangoUnchained.Views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.Utils.ScoreInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.LabelFactory;

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
        
        Label titleLabel = LabelFactory.createLabel("High Scores", getSkin(), "titleFont", Color.BLACK);


        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        table.add(titleLabel).center().padBottom(50);
        table.row();

        // Add a loading message while fetching scores
        LabelFactory.createLabel("Loading scores...", getSkin(), "defaultFont", null, 300, 60, 10, table).center();

        stage.addActor(table);

        // FirebaseManager is platform specific !
        FirebaseManager firebaseManager = GameController.getInstance().getFirebaseManager();

        // Fetch scores from Firebase asynchronously
        // TODO: Add functionality to fetch scores for different levels (currently hardcoded to 1)
        firebaseManager.loadScores(1, new FirebaseManager.Callback<List<ScoreInfo>>() {
            @Override
            public void onSuccess(final List<ScoreInfo> scores) {
                table.clear();
                table.add(titleLabel).center().padBottom(50);
                table.row();

                if (scores.isEmpty()) {
                    LabelFactory.createLabel("No Scores found.", getSkin(), "defaultFont", null, 300, 60, 10, table).center();
                } else {
                    for (int i = 0; i < scores.size(); i++) {
                        ScoreInfo entry = scores.get(i);
                        String scoreText = (i + 1) + ". " + entry.displayName + ": " + entry.score + " points";
                        LabelFactory.createLabel(scoreText, getSkin(), "rioGrandeFont", Color.BLACK, 300, 60, 10, table).center();
                    }
                }

                ButtonFactory.createButton("Back", 300, 60, getSkin(), game,
                        () -> game.setView(new MainMenuView()), "customLoginStyle", table);
            }

            @Override
            public void onError(final Exception e) {
                table.clear();
                table.add(titleLabel).center().padBottom(50);
                table.row();

                LabelFactory.createLabel("Error loading scores: " + e.getMessage(), getSkin(), "defaultFont", Color.RED, 300, 60, 10, table).center();

                ButtonFactory.createButton("Back", 300, 60, getSkin(), game,
                        () -> game.setView(new MainMenuView()), "customLoginStyle", table);
            }
        });

    }
}
