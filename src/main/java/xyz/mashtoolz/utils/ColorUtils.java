package xyz.mashtoolz.utils;

public class ColorUtils {

    public static int hex2Int(String colorHex, int opacity) {
        colorHex = colorHex.replace("#", "");
        if (colorHex.length() != 6)
            throw new IllegalArgumentException("Invalid hex color format.");
        return (opacity << 24) | Integer.parseInt(colorHex, 16);
    }

    public static String int2Hex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static int blend(int startColor, int endColor, float ratio) {
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        int r = (int) (startR + (endR - startR) * ratio);
        int g = (int) (startG + (endG - startG) * ratio);
        int b = (int) (startB + (endB - startB) * ratio);

        return (r << 16) | (g << 8) | b;
    }

    public static int darkenColor(int color, double darkenFactor) {
        // Extract the RGB components
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        // Apply the darken factor
        red = (int) (red * darkenFactor);
        green = (int) (green * darkenFactor);
        blue = (int) (blue * darkenFactor);

        // Ensure the values stay within the valid range [0, 255]
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        // Combine the components back into a single color int
        return (red << 16) | (green << 8) | blue;
    }
}
