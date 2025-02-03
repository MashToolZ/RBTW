package xyz.mashtoolz.rbtw;

import net.minecraft.entity.player.PlayerEntity;
import xyz.mashtoolz.config.TBTW;
import xyz.mashtoolz.utils.ColorUtils;

public class Layer {

    public static Layer SURFACE = new Layer("Surface", 0xD1D1D1, 210, 210, 1.0);
    public static Layer SAND = new Layer("Sand", 0xE7D4C8, 136, 210, 1.0);
    public static Layer STONE = new Layer("Stone", 0x646464, 40, 136, 2.0);
    public static Layer NETHER = new Layer("Nether", 0xFD3434, 0, 40, 5.0);

    public static Layer RED_SAND = new Layer("Red Sand", 0xD99632, 136, 210, 20.0);
    public static Layer DEEPSLATE = new Layer("Deepslate", 0x616264, 40, 136, 100.0);
    public static Layer THE_END = new Layer("The End", 0xEEF4BC, 0, 40, 500.0);

    public String name;
    public int color;
    public double min, max;
    public double drainRate;

    public Layer(String name, int color, double min, double max, double drainRate) {
        this.name = name;
        this.color = color;
        this.min = min;
        this.max = max;
        this.drainRate = drainRate;
    }

    public int fillColor() {
        return ColorUtils.hex2Int("#" + String.format("%06X", color), 192);
    }

    public String textColor() {
        return String.format("<#%06X>", color);
    }

    public static int min() {
        return (int) Math.min(SURFACE.min, Math.min(SAND.min, Math.min(STONE.min, NETHER.min)));
    }

    public static int max() {
        return (int) Math.max(SURFACE.max, Math.max(SAND.max, Math.max(STONE.max, NETHER.max)));
    }

    public static Layer getLayer(PlayerEntity player) {
        double playerY = player.getY();

        if (TBTW.ALTERNATE.isInside(player.getBlockPos()) || TBTW.DEV_ALTERNATE.isInside(player.getBlockPos())) {
            if (playerY > SURFACE.min) return SURFACE;
            if (playerY > RED_SAND.min) return RED_SAND;
            if (playerY > DEEPSLATE.min) return DEEPSLATE;
            return THE_END;
        }

        if (playerY > SURFACE.min) return SURFACE;
        if (playerY > SAND.min) return SAND;
        if (playerY > STONE.min) return STONE;
        return NETHER;
    }
}

// 104115, 4115 to 104185, 4185