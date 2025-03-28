package io.github.RangoUnchained.Views.Utils;

public class Constants {
    // Pixels per meter ratio (recommended: 32-100)
    public static final float PPM = 100f;

    // Convert from Box2D world coordinates to screen coordinates
    public static float metersToPixels(float meters) {
        return meters * PPM;
    }

    // Convert from screen coordinates to Box2D world coordinates
    public static float pixelsToMeters(float pixels) {
        return pixels / PPM;
    }
}