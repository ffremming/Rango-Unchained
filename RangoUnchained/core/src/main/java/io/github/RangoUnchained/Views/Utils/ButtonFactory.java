package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.level.GameFileHandler;

public class ButtonFactory {

    public static TextButton createButton(String text, float width, float height, Skin skin, GameController game, Runnable onClickAction) {
        TextButton button = new TextButton(text, skin);
        button.setSize(width, height);

        if (text.startsWith("Level")) {
            int level = Integer.parseInt(text.replace("Level ", ""));
            if (level > GameFileHandler.getInstance().getProgress()) {
                Gdx.app.log("ButtonFactory", "Level " + level + " is locked."+ GameFileHandler.getInstance().getProgress());
                button.setDisabled(true);
                button.setColor(0.5f, 0.5f, 0.5f, 1); // Set to a gray color to indicate it's disabled
                return button;
            }
        }

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClickAction != null) {
                    onClickAction.run();
                }
            }
        });

        return button;
    }
}
