package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LabelFactory {
    
    // Basic label creation
    public static Label createLabel(String text, Skin skin, String fontName, Color color) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = skin.getFont(fontName);
        if (color != null)
            style.fontColor = color;
        return new Label(text, style);
    }

    // Label creation and styling inside a table
    public static Table createLabel(String text, Skin skin, String fontName, Color color, float width, float height, float padding, Table table) {
        Label label = createLabel(text, skin, fontName, color);
        table.add(label)
            .padBottom(padding);
        table.row();
        return table;
    }
}
