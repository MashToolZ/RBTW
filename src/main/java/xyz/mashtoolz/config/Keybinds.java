package xyz.mashtoolz.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import xyz.mashtoolz.RBTW;
import xyz.mashtoolz.mixins.KeyBindingAccessor;

public class Keybinds {

    public static void load() {
        RBTW.log("Loaded Keybinds", true);
    }

    public static KeyBinding menu = add("rbtw.key.menu", GLFW.GLFW_KEY_UNKNOWN);

    public static KeyBinding add(String key, int defaultKey) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(key, InputUtil.Type.KEYSYM, defaultKey, "RBTW"));
    }

    public static boolean isPressed(KeyBinding keybind) {
        int code = ((KeyBindingAccessor) keybind).getBoundKey().getCode();
        return code != -1 && InputUtil.isKeyPressed(RBTW.getClient().getWindow().getHandle(), code);
    }

    public static void update() {

        while (menu.wasPressed())
            menu();
    }

    private static void menu() {
        MinecraftClient client = RBTW.getClient();
        if (client == null) return;
        client.setScreen(AutoConfig.getConfigScreen(CBTW.class, client.currentScreen).get());
    }
}
