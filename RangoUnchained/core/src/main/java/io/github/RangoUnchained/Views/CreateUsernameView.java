package io.github.RangoUnchained.Views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.Firebase.FirebaseManager;
import io.github.RangoUnchained.Model.Firebase.Utils.UserInfo;
import io.github.RangoUnchained.Views.Utils.BaseScreen;
import io.github.RangoUnchained.Views.Utils.ButtonFactory;
import io.github.RangoUnchained.Views.Utils.LabelFactory;
import io.github.RangoUnchained.Views.Utils.TextFieldFactory;

public class CreateUsernameView extends BaseScreen {

    private Table usernameFieldContainer;
    private TextField usernameField; // Store the TextField reference
    private Label errorLabel;

    public CreateUsernameView() {
        super(GameController.getInstance());
    }

    @Override
    public void show() {
        super.show();
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true); // Make the table fill the entire stage
        table.top().padTop(20);
        table.defaults().center();
        table.add().expandX(); // helps make the table take full width
        table.center().row();

        //Create the TextField first
        TextField textField = new TextField("", getSkin(), "textFieldStyle-textField");
        this.usernameField = textField; // Save reference to the actual TextField
        Label titleLabel = LabelFactory.createLabel("Create a New Username", getSkin(), "titleFont", Color.BLACK);
        usernameFieldContainer = TextFieldFactory.createTextField(getSkin(), "", "textFieldStyle-textField", "textfield", true, 60, 300, 90, usernameField);
            
        table.add(titleLabel)           
        .width(300)
        .height(60)
        .center()
        .padBottom(20);
        table.row();
        errorLabel = LabelFactory.createLabel("", getSkin(), "errorFont", null);
        errorLabel.setWrap(true);
        errorLabel.setWidth(500);

        table.add(LabelFactory.createLabel("New Username:", getSkin(), "defaultFont", null)).center().padBottom(5);
        table.row();
        table.add(usernameFieldContainer)            
        .width(300)
        .height(60)
        .center()
        .padBottom(20)
        .padTop(20);
        table.row();

        table.add(createConfirmButton())            
        .width(300)
        .height(60)
        .center()
        .padBottom(20);
        table.row();
        table.add(errorLabel)            
        .width(500)
        .center()
        .padBottom(20);
        table.row();
        ButtonFactory.createDefaultButton("back", () -> game.setView(new MainMenuView()), table);
        table.row();
        stage.addActor(table);
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }

    private Button createConfirmButton() {
        return ButtonFactory.createButton("Confirm", getSkin(), game, () -> {
            String username = usernameField.getText().trim();

            if (username.isEmpty()) {
                displayError("Username cannot be empty");
                return;
            }

            game.getFirebaseManager().createUsername(username, new FirebaseManager.Callback<UserInfo>() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    game.setUserInfo(userInfo);
                    game.setView(new MainMenuView());
                }

                @Override
                public void onError(Exception e) {
                    displayError(e.getMessage());
                }
            });
        }, "customLoginStyle");
    }
}
