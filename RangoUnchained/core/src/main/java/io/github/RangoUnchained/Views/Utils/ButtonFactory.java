package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.RangoUnchained.Controllers.GameController;
import io.github.RangoUnchained.Model.level.GameFileHandler;

public class ButtonFactory {

    public static TextButton createButton(String text, Skin skin, GameController game, Runnable onClickAction, String styleName) {
        TextButton button = styleName != null
            ? new TextButton(text, skin, styleName)
            : new TextButton(text, skin);
    
        if (text.startsWith("Level")) {
            int level = Integer.parseInt(text.replace("Level ", ""));
            if (level > GameFileHandler.getInstance().getProgress()) {
                Gdx.app.log("ButtonFactory", "Level " + level + " is locked." + GameFileHandler.getInstance().getProgress());
                button.setDisabled(true);
                button.setColor(0.5f, 0.5f, 0.5f, 1);
                return button;
            }
        }
    
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onClickAction != null) onClickAction.run();
            }
        });
        return button;
    }
    public static Table createButton(String text, float width, float height, Skin skin, GameController game, Runnable onClickAction, String styleName, Table table) {
        TextButton button = createButton(text, skin, game, onClickAction, styleName);
        table.add(button)            
        .width(width)
        .height(height)
        .center()
        .padBottom(20);
        table.row();
        return table;
    }

    public static Table createDefaultButton(String text, Runnable onClickAction, Table table){
        return createButton(text, 300, 60, GameController.getSkin(), GameController.getInstance(), onClickAction, "customLoginStyle", table);
    }
    
}
