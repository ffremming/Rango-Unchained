package io.github.RangoUnchained.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.UserInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;

public class LoginView extends BaseScreen {

    private TextField emailField;
    private TextField passwordField;
    private Label errorLabel;

    public LoginView() {
        super(GameController.getInstance());
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        // Create UI elements
        Label titleLabel = new Label("Log In", getSkin());
        emailField = new TextField("", getSkin());
        passwordField = new TextField("", getSkin());
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // Create a table to align UI-elements
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        // Back to main button
        table.add(ButtonFactory.createButton("Back", 300, 60, getSkin(), game,
            () -> game.setView(new MainMenuView()))).left();
        table.row();
        table.add(titleLabel).center().padBottom(20);
        table.row();

        // Email field
        table.add(new Label("Email", getSkin())).left().padBottom(5);
        table.row();
        table.add(emailField).width(300).padBottom(15);
        table.row();

        // Password field
        table.add(new Label("Password", getSkin())).left().padBottom(5);
        table.row();
        table.add(passwordField).width(300).padBottom(10);
        table.row();
        table.add(createLoginButton()).center().padBottom(20);
        table.row();

        // Error label
        errorLabel = new Label("", getSkin());
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setWrap(true);
        errorLabel.setWidth(300);
        table.add(errorLabel).width(300).padBottom(10);
        table.row();

        // Switch scene to login
        table.row();
        table.add(new Label("Don't have a user?", getSkin())).center().padBottom(10);
        table.row();
        table.add(ButtonFactory.createButton("Create User", 300, 60, getSkin(), game,
            () -> game.setView(new CreateUserView()))).center().padBottom(20);
        stage.addActor(table);
    }

    private void displayError(Exception e) {
        errorLabel.setText(e.getMessage());
    }

    private Button createLoginButton() {
        return ButtonFactory.createButton("Log In", 300, 60, getSkin(), game, () -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty()) {
                displayError(new IllegalArgumentException("Email cannot be empty."));
                return;
            }
            if (password.isEmpty()) {
                displayError(new IllegalArgumentException("Password cannot be empty"));
                return;
            }

            game.getFirebaseManager().logIn(email, password, new FirebaseManager.Callback<UserInfo>() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    game.setIsLoggedIn(true);
                    game.setUserInfo(userInfo);
                    game.setView(new MainMenuView());
                }

                @Override
                public void onError(Exception e) {
                    displayError(e);
                }
            });
        });
    }

}
