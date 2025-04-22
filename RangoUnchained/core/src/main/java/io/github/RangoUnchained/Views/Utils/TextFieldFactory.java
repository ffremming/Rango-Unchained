package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Utility factory for creating styled and optionally transparent TextFields wrapped in containers.
 */
public class TextFieldFactory {

    public static Table createTextField(
        Skin skin, String initialText, String textFieldStyleName, String containerDrawableName,
        boolean transparentBackground, float padding, float width, float height,
        TextField existingTextField) {

        // Container with background
        Drawable frameDrawable = skin.getDrawable(containerDrawableName);
        Table fieldContainer = new Table();
        fieldContainer.background(frameDrawable);

        // Use the provided TextField or create a new one
        TextField textField = existingTextField != null ? existingTextField :
                            new TextField(initialText, skin, textFieldStyleName);

        if (transparentBackground) {
            TextFieldStyle transparentStyle = new TextFieldStyle(skin.get(textFieldStyleName, TextFieldStyle.class));
            transparentStyle.background = null;
            textField.setStyle(transparentStyle);
        }

        // Add TextField to container
        fieldContainer.add(textField)
                .width(width-padding)
                .height(height-padding)
                .fill();

        // Wrap container with proper size
        Table outerContainer = new Table();
        outerContainer.add(fieldContainer)
                .width(width)
                .height(height)
                .center()
                .padBottom(20);
        return outerContainer;
    }
}
