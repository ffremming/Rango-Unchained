package io.github.RangoUnchained.Views.Utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import io.github.RangoUnchained.Controllers.GameController;

public class SliderFactory {
   
    public static Table createVolumeSlider(GameController game, Skin skin) {
        // Main container
        Table sliderContainer = new Table();
        
        // Get the default slider style
        Slider.SliderStyle originalStyle = skin.get("default-horizontal", Slider.SliderStyle.class);
        
        // Create a copy of the style for the right slider with modifications
        Slider.SliderStyle rightSliderStyle = new Slider.SliderStyle();
        rightSliderStyle.knob = originalStyle.knob;
        rightSliderStyle.knobOver = originalStyle.knobOver;
        rightSliderStyle.knobDown = originalStyle.knobDown;
        
        // Make the background invisible for the actual slider
        rightSliderStyle.background = skin.newDrawable("white", 0, 0, 0, 0); // Transparent background
        
        // Modify knob size if needed
        if (rightSliderStyle.knob instanceof SpriteDrawable) {
            SpriteDrawable originalKnob = (SpriteDrawable) rightSliderStyle.knob;
            Sprite knobSprite = new Sprite(originalKnob.getSprite());
            knobSprite.setSize(40, 40);
            SpriteDrawable largeKnob = new SpriteDrawable(knobSprite);
            rightSliderStyle.knob = largeKnob;
        }
        
        // Create a Table for the visual background (left + right portions combined)
        Table backgroundTable = new Table();
        backgroundTable.setBackground(originalStyle.background);
        backgroundTable.setHeight(60); // Your desired height
        
        // Create the actual slider for just the right portion
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, rightSliderStyle);
        volumeSlider.setValue(game.getMusicVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newVolume = volumeSlider.getValue();
                game.setMusicVolume(newVolume);
            }
        });
        
        // Define proportions
        float totalWidth = 300; // Total slider width
        float leftPortion = 0.25f; // How much of the slider is inactive (left side)
        float rightPortion = 1 - leftPortion-0.03f; // Active portion (right side)
        
        // Add background to container
        sliderContainer.add(backgroundTable).width(totalWidth).height(60);
        
        // Position the actual slider over the right portion of the background
        sliderContainer.addActor(volumeSlider);
        volumeSlider.setPosition(
            totalWidth * leftPortion, // X position starts at left portion's end
            0 // Y position
        );
        volumeSlider.setSize(totalWidth * rightPortion, 60);
        
        return sliderContainer;
    }
}