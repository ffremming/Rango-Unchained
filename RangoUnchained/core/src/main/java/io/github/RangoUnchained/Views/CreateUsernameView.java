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

public class CreateUsernameView extends BaseScreen {

    private TextField usernameField;
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
        Label titleLabel = new Label("Create a New Username", getSkin());
        usernameField = new TextField("", getSkin());

        errorLabel = new Label("", getSkin());
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setWrap(true);
        errorLabel.setWidth(300);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);

        table.add(ButtonFactory.createButton("back", 300, 60, getSkin(), game,
            () -> game.setView(new MainMenuView()))).left();
        table.row();
        table.add(titleLabel).center().padBottom(20);
        table.row();

        table.add(new Label("New Username:", getSkin())).left().padBottom(5);
        table.row();
        table.add(usernameField).width(300).padBottom(15);
        table.row();

        table.add(createConfirmButton()).center().padBottom(15);
        table.row();
        table.add(errorLabel).width(300).padBottom(10);
        table.row();

        stage.addActor(table);
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }

    private Button createConfirmButton() {
        return ButtonFactory.createButton("Confirm", 300, 60, getSkin(), game, () -> {
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
        });
    }
}
