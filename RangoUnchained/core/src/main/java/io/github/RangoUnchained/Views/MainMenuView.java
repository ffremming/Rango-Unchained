package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.UserInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class MainMenuView extends BaseScreen {

    public MainMenuView() {
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
        Label titleLabel = new Label("Rango Unchained", labelStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        table.add(titleLabel).center().padBottom(30);
        table.row();

        // Display login state
        String userState = game.getIsLoggedIn() ? "Play" : "Play as guest";

        // Add buttons
        if (!game.getIsLoggedIn()){
            table.add(ButtonFactory.createButton("Log In", 300, 60, getSkin(), game,  () -> game.setView(new LoginView()))).center().padBottom(20);
            table.row();
        }

        table.add(ButtonFactory.createButton(userState, 300, 60, getSkin(), game,
            () -> game.setView(new SelectLevelView()))).center().padBottom(20);
        table.row();
        table.add(ButtonFactory.createButton("Scoreboard", 300, 60, getSkin(), game,
            () -> game.setView(new ScoreboardView()))).center().padBottom(20);
        table.row();
        // Add log out button if user is logged in
        if (game.getIsLoggedIn()) {
            table.add(ButtonFactory.createButton("Change username", 300, 60, getSkin(), game,
                () -> game.setView(new CreateUsernameView()))).center().padBottom(50);
            table.row();
            // Display logged in user
            table.add(new Label("Logged in as: " + game.getCurrentUserInfo().getDisplayName(), labelStyle)).padBottom(20);
            table.row();
            table.add(ButtonFactory.createButton("Log Out", 300, 60, getSkin(), game, () -> {
                game.getFirebaseManager().signOut();
                game.setIsLoggedIn(false);
                game.setView(new MainMenuView());
            })).center();
        }
        // Add table to stage
        stage.addActor(table);
    }
}
