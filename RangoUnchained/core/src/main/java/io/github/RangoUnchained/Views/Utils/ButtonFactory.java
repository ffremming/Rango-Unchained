package io.github.RangoUnchained.Views.Utils;

import static io.github.RangoUnchained.Views.Utils.ViewFactory.createView;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.RangoUnchained.Controllers.GameController;

public class ButtonFactory {

    public static TextButton createButton(String text, float width, float height, Skin skin, GameController game, String nextScreen) {
        TextButton button = new TextButton(text, skin);
        button.setSize(width, height);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setView(createView(nextScreen), "");
            }
        });
        return button;
    }

}
