package me.pvpb0t.util;

import java.awt.*;

public class ColorUtil {
    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState %= 360.0) / 360.0), (float) (255 / 255.0f), (float) (255 / 255.0f));
    }

}
