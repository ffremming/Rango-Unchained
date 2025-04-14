package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.HintUtil;

public class GameOverView extends BaseScreen {

    int levelNumber;
    int score;
    private Label scoreLabel;

    boolean completed;
    public GameOverView(int levelNumber, boolean completed) {
        super(GameController.getInstance());
        this.levelNumber = levelNumber;
        this.score = LevelController.getInstance().getScore();
        this.completed = completed;
    }

    @Override
    public void show() {
        super.show();
        createUI();
        saveScoreToFirebase(this.levelNumber);
    }

    private void saveScoreToFirebase(int levelNumber) {
        game.getFirebaseManager().updateScoreForLevel(GameController.getInstance().getCurrentUser(), levelNumber, score, new FirebaseManager.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean isHighscore) {
                if (isHighscore) {
                    scoreLabel.setText("New highscore! Score: " + score);
                } else {
                    scoreLabel.setText("Score: " + score);
                }
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Failed to save score: " + e.getMessage());
                scoreLabel.setText("Score: " + score);
            }
        });
    }

    private void createUI() {
        // Add background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("Background/Background.png")); // Replace with your actual image path
        Image backgroundImage = new Image(backgroundTexture);

        // Make the image fill the screen
        backgroundImage.setFillParent(true);

        // Add to stage first so it's behind everything else
        stage.addActor(backgroundImage);
       
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        String titleText = completed ? "Level Completed" : "Level Failed";

        Label titleLabel = new Label(titleText, labelStyle);
        scoreLabel = new Label("", getSkin());

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Title label
        table.add(titleLabel).center().padBottom(10);
        table.row();
        // Score label
        table.add(scoreLabel).center().padBottom(10);
        table.row();
        // Hint label
        Label hintLabel = new Label(HintUtil.getHint(), getSkin());
        table.add(hintLabel).center().padBottom(50);
        table.row();



        // Retry button (goes back to level selection)
        ButtonFactory.createButton("Play again", 300, 60, getSkin(),  game, () -> game.setView(new SelectLevelView()), "customLoginStyle", table);

         // Retry button (goes back to level selection)
         if (levelNumber < 5 && (completed || GameFileHandler.getInstance().getProgress() > levelNumber)) {
            ButtonFactory.createButton("Next level", 300, 60, getSkin(),  game, () -> game.setView(new GamePlayView(levelNumber+1)), "customLoginStyle", table);
         }

        ButtonFactory.createButton("Scoreboard", 300, 60, getSkin(),  game, () -> game.setView(new ScoreboardView()), "customLoginStyle", table);

        // Back to main menu button
        ButtonFactory.createButton("Main Menu", 300, 60, getSkin(), game,() -> game.setView(new MainMenuView()), "customLoginStyle", table);

        stage.addActor(table);
    }
}
