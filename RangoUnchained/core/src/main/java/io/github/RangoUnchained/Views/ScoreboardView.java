package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.Utils.ScoreInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.ScrollUtil;

/**
 * Split‑panel scoreboard that shows *all* levels (even locked) on the left and the leaderboard for the
 * currently selected level on the right.  Clicking a level button highlights it and refreshes the leaderboard.
 */
public class ScoreboardView extends BaseScreen {
    private int selectedLevel = 1; // Initial default level
    private TextButton currentlySelectedButton;
    private final Map<Integer, TextButton> levelButtons = new HashMap<>();
    private Table leaderboardTable;
    private ScrollPane leaderboardScroll;
    private final FirebaseManager firebaseManager;
    private static final Color HIGHLIGHT_COLOR = Color.BROWN;
    private static final Color NORMAL_COLOR    = Color.WHITE;

    public ScoreboardView() {
        super(GameController.getInstance());
        this.firebaseManager = GameController.getInstance().getFirebaseManager();
    }

    @Override
    public void show() {
        super.show();
        createUI();
        // Load and display scores for selected level
        refreshLeaderboard();
    }

    private void createUI() {
        // Root table
        Table root = new Table();
        root.setFillParent(true);
        LabelFactory.createLabel("High Scores", getSkin(), "titleFont", Color.BLACK, TITLE_PADDING, root);

        Table split = new Table();

        // Left – level list
        Table levelButtonsTable = buildLevelButtonsTable();
        ScrollPane levelsScroll = ScrollUtil.createStyledScrollPane(levelButtonsTable);
        levelsScroll.setFadeScrollBars(false);

        // Right – leaderboard
        leaderboardTable = new Table();
        leaderboardTable.top().padTop(20);
        leaderboardTable.defaults().center();
        leaderboardScroll = ScrollUtil.createStyledScrollPane(leaderboardTable);
        leaderboardScroll.setFadeScrollBars(false);

        split.add(levelsScroll).center().width(450).expandY().fillY().padRight(30);
        split.add(leaderboardScroll).expand().fill();

        root.row();
        root.add(split).expand().fill();

        // Back button
        root.row();
        ButtonFactory.createButton(
            "Back", BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_PADDING,
            getSkin(), game, () -> game.setView(new MainMenuView()),
            "customLoginStyle", root);

        stage.addActor(root);
    }

    /**
     * Builds and returns a vertical table with one button per level (0..LEVELS_COUNT), regardless of unlock state.
     * Buttons are cached in {@link #levelButtons} so we can easily update colours on selection change.
     */
    private Table buildLevelButtonsTable() {
        levelButtons.clear();

        Table table = new Table();
        table.top().padTop(20);
        table.defaults().center();
        table.add().expandX();
        table.row();

        for (int i = 0; i <= Constants.LEVELS_COUNT; i++) {
            final int level = i;
            String buttonText = (i == 0) ? "Tutorial" : "level " + i;

            // Create button with consistent style
            TextButton btn = ButtonFactory.createButton(
                buttonText,
                getSkin(),
                game,
                () -> onLevelSelected(level),
                "customLoginStyle");

            table.add(btn).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
            table.row();

            levelButtons.put(level, btn);

            if (level == selectedLevel) {
                highlightButton(btn);
                currentlySelectedButton = btn;
            }
        }

        table.padLeft(50);
        return table;
    }

    private void onLevelSelected(int level) {
        if (selectedLevel == level) return;

        // Remove highlight from previous button (if any)
        if (currentlySelectedButton != null) {
            unHighlightButton(currentlySelectedButton);
        }

        // Highlight new button
        TextButton newBtn = levelButtons.get(level);
        highlightButton(newBtn);
        currentlySelectedButton = newBtn;

        // Update state & reload leaderboard
        selectedLevel = level;
        refreshLeaderboard();
    }

    private void highlightButton(TextButton btn) {
        if (btn != null) btn.setColor(HIGHLIGHT_COLOR);
    }

    private void unHighlightButton(TextButton btn) {
        if (btn != null) btn.setColor(NORMAL_COLOR);
    }

    private void refreshLeaderboard() {
        leaderboardTable.clear();
        LabelFactory.createLabel("Loading scores…", getSkin(), "defaultFont", null, 300, leaderboardTable).center();

        firebaseManager.loadScores(selectedLevel, new FirebaseManager.Callback<List<ScoreInfo>>() {
            @Override
            public void onSuccess(List<ScoreInfo> scores) {
                Gdx.app.postRunnable(() -> displayScores(scores));
            }

            @Override
            public void onError(Exception e) {
                Gdx.app.postRunnable(() -> {
                    leaderboardTable.clear();
                    LabelFactory.createLabel("Error loading scores: " + e.getMessage(), getSkin(), "defaultFont", Color.RED, 10, leaderboardTable).center();
                });
            }
        });
    }

    private void displayScores(List<ScoreInfo> scores) {
        leaderboardTable.clear();

        if (scores.isEmpty()) {
            LabelFactory.createLabel("No Scores found.", getSkin(), "defaultFont", null, 30, leaderboardTable).center();
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreInfo s = scores.get(i);
                String txt = (i + 1) + ". " + s.displayName + ": " + s.score + " points";
                LabelFactory.createLabel(txt, getSkin(), "rioGrandeFont", Color.BLACK, 20, leaderboardTable).center();
            }
        }

        // Reset scroll position so the top rank is visible
        Gdx.app.postRunnable(() -> {
            leaderboardScroll.layout();
            leaderboardScroll.setScrollY(0);
            leaderboardScroll.updateVisualScroll();
        });
    }
}
