package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ScrollUtil {

    public static ScrollPane createStyledScrollPane(Table table) {
        // Transparent scrollbar
        Pixmap barPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        barPixmap.setColor(1f, 1f, 1f, 0.3f); // White with 30% transparency
        barPixmap.fill();
        Texture barTexture = new Texture(barPixmap);
        Drawable barDrawable = new TextureRegionDrawable(new TextureRegion(barTexture));

        // Solid white knob
        Pixmap knobPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(Color.WHITE);
        knobPixmap.fill();
        Texture knobTexture = new Texture(knobPixmap);
        Drawable knobDrawable = new TextureRegionDrawable(new TextureRegion(knobTexture));

        // Style it
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScroll = barDrawable;
        scrollStyle.vScrollKnob = knobDrawable;
        

        // Optional: tweak size
        scrollStyle.vScroll.setMinWidth(15);
        scrollStyle.vScrollKnob.setMinWidth(15);
        scrollStyle.vScrollKnob.setMinHeight(30);

        
        ScrollPane scrollPane = new ScrollPane(table, scrollStyle);
        scrollPane.setFadeScrollBars(false);        
        scrollPane.setScrollbarsOnTop(true);        
        scrollPane.setForceScroll(false, true);     

        return scrollPane;
    }
}
