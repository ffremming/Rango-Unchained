package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.TextFieldFactory;

public class LoginView extends BaseScreen {

    private Table emailFieldContainer;
    private TextField emailField;
    private Table passwordFieldContainer;
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
        passwordField =  new TextField("", getSkin(), "textFieldStyle-textField");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('-');

        // Create a table to align UI-elements
        Table table = new Table();
        table.top().padTop(20);
        table.defaults().padLeft(20).center();


        LabelFactory.createLabel("Log In", getSkin(), "titleFont", Color.BLACK, BUTTON_WIDTH, 60, 20, table);
        
        table.add(LabelFactory.createLabel("Email", getSkin(), "defaultFont", null)).row();
        
        TextField textField = new TextField("", getSkin(), "textFieldStyle-textField");
        this.emailField = textField; // Save reference to the actual TextField
        emailFieldContainer = TextFieldFactory.createTextField(getSkin(), "", "textFieldStyle-textField", "textfield", true, 60, BUTTON_WIDTH, 90, emailField);

        table.add(emailFieldContainer).row();
 // Password field
LabelFactory.createLabel("Password", getSkin(), "defaultFont", null, BUTTON_WIDTH, 60, 5, table);
table.row();

        passwordFieldContainer = TextFieldFactory.createTextField(
            getSkin(),
            "123@123",
            "textFieldStyle-textField",
            "textfield",
            true,     // transparent background
            60,       // inner padding
            BUTTON_WIDTH,      // width
            90,        // height
            passwordField
        );
        table.add(passwordFieldContainer);
        table.row();

        table.add(createLoginButton())
        .width(BUTTON_WIDTH)
        .height(60)
        .center()
        .padBottom(20).row();
        Label noUserLabel = LabelFactory.createLabel("Don't have a user?", getSkin(), "defaultFont", null);
        table.add(noUserLabel).center().padBottom(10).row();

       // Switch scene to create user

        ButtonFactory.createButton("Create User", BUTTON_WIDTH, 60, getSkin(), game,
        () -> game.setView(new CreateUserView()), "customLoginStyle", table);



        // Error label
        errorLabel = LabelFactory.createLabel("", getSkin(), "defaultFont", null);
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setWrap(true);
        errorLabel.setWidth(BUTTON_WIDTH);
        table.add(errorLabel).width(BUTTON_WIDTH).padBottom(10);
        table.row();

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
        ButtonFactory.createDefaultButton("Back to meny",
        () -> game.setView(new MainMenuView()), mainTable);
 


        stage.addActor(mainTable);

    }

    private void displayError(Exception e) {
        errorLabel.setText(e.getMessage());
    }

    private Button createLoginButton() {
        return ButtonFactory.createButton("Log In", getSkin(), game, () -> {
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
        }, "customLoginStyle");
    }

}
