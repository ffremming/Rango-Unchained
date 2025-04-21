package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class FontUtils {

    public static void addFontAndTextButtonStyleToSkin(
        Skin skin, String fontName, String styleName, String fontPath, int fontSize, Color fontColor) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.color = fontColor;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        skin.add(fontName, font, BitmapFont.class);

        // ---- TextButtonStyle ----
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = fontColor;
        try {
            textButtonStyle.up = skin.getDrawable("button");
            textButtonStyle.down = skin.getDrawable("button-down");
            textButtonStyle.over = skin.getDrawable("button-over");
        } catch (Exception e) {
            // Fallback: use 'default' drawable if missing
            textButtonStyle.up = skin.getDrawable("default-round"); // adjust to what your skin supports
        }
        
        skin.add(styleName, textButtonStyle);

        // ---- TextFieldStyle ----
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = fontColor;

        // You must have these drawables in your skin atlas (or replace with your actual drawable names)
        try {
            textFieldStyle.background = skin.getDrawable("textfield");
            textFieldStyle.cursor = skin.getDrawable("cursor");
            textFieldStyle.selection = skin.getDrawable("selection");
        } catch (Exception e) {
            // Fallback for text field
            textFieldStyle.background = skin.getDrawable("default");
            textFieldStyle.cursor = skin.getDrawable("default");
            textFieldStyle.selection = skin.getDrawable("default");
        }
        

        // Register it with a similar name
        skin.add(styleName + "-textField", textFieldStyle);
    }
}
