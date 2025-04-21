package io.github.RangoUnchained.Views;

import com.badlogic.gdx.Gdx;
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

public class CreateUserView extends BaseScreen {

    private TextField emailField;
    private Table emailFieldContainer;
    private Table passwordFieldContainer;
    private TextField passwordField;
    private Label errorLabel;

    public CreateUserView() {
        super(GameController.getInstance());
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        // Create UI elements


        // Create a table to align UI-elements
        Table table = new Table();
        table.top().padTop(20);
        table.defaults().padLeft(20).center();

        LabelFactory.createLabel("Create User", getSkin(), "titleFont", null, 0, 0, 20, table); 
        table.row();

        table.add(LabelFactory.createLabel("Email", getSkin(), "defaultFont", null)).row();
        emailField =  new TextField("", getSkin(), "textFieldStyle-textField");

        emailFieldContainer = TextFieldFactory.createTextField(            
        getSkin(),
        "",
        "textFieldStyle-textField",
        "textfield",
        true,     // transparent background
        60,       // inner padding
        300,      // width
        90,
        emailField
        );      
        table.add(emailFieldContainer).row();
        // Password field;
        LabelFactory.createLabel("Password", getSkin(), "defaultFont", null, 0, 0, 5, table).row();
        
        passwordField =  new TextField("", getSkin(), "textFieldStyle-textField");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('-');
        passwordFieldContainer =  TextFieldFactory.createTextField(            
            getSkin(),
            "",
            "textFieldStyle-textField",
            "textfield",
            true,     // transparent background
            60,       // inner padding
            300,      // width
            90,
            passwordField
            );       
        table.add(passwordFieldContainer);
        
        table.row();
        table.add(createSignUpButton()).width(300).height(60).center().padBottom(20);
        table.row();

        // Error label
        errorLabel = LabelFactory.createLabel("", getSkin(), "defaultFont", null);
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setWrap(true);
        errorLabel.setWidth(300);
        table.add(errorLabel).width(300).padBottom(10);
        table.row();

        // Switch scene to login
        table.add(LabelFactory.createLabel("Already have a user?", getSkin(), "defaultFont", null));    


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
        ButtonFactory.createButton("Back to Login", 300, 60, getSkin(), game,
            () -> game.setView(new LoginView()), "customLoginStyle", mainTable);

        stage.addActor(mainTable);
    }

    private void displayError(Exception e) {
        errorLabel.setText(e.getMessage());
    }

    private Button createSignUpButton() {
        return ButtonFactory.createButton("Sign Up",  getSkin(), game, () -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty()) {
                displayError(new IllegalArgumentException("Email cannot be empty."));
                return;
            }
            if (password.length() < 6) {
                displayError(new IllegalArgumentException("Password must be at least 6 characters."));
                return;
            }
            if (!email.contains("@") || !email.contains(".") || email.indexOf('@') > email.lastIndexOf('.') || email.indexOf('@') == 0 || email.lastIndexOf('.') == email.length() - 1) {
                displayError(new IllegalArgumentException("Not a valid email address."));
                return;
            }

            game.getFirebaseManager().createUser(email, password, new FirebaseManager.Callback<UserInfo>() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    game.setIsLoggedIn(true);
                    game.setUserInfo(userInfo);
                    game.setView(new CreateUsernameView());
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("Signup failed: " + e.getMessage());
                    displayError(e);
                }
            });
        }, "customLoginStyle");
    }
}
