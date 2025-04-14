package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.level.GameFileHandler;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.Constants;

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
        // Add background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("Background/Background.png")); // Replace with your actual image path
        Image backgroundImage = new Image(backgroundTexture);

        // Make the image fill the screen
        backgroundImage.setFillParent(true);

        // Add to stage first so it's behind everything else
        stage.addActor(backgroundImage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RioGrande.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 62; // Set desired font size
        parameter.color = Color.BLACK;
        parameter.spaceX = 2;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Dispose to prevent memory leaks
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label titleLabel = new Label("Select Level", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        table.add(titleLabel).center().padBottom(50);
        table.row();

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
        ButtonFactory.createButton("Back", 300, 60, getSkin(), game, () -> game.setView(new MainMenuView()), "customLoginStyle", table);

        stage.addActor(table);
    }
}
