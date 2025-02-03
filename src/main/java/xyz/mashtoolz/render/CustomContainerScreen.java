package xyz.mashtoolz.render;

import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;

public class CustomContainerScreen extends GenericContainerScreen {

    public CustomContainerScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text customTitle) {
        super(handler, inventory, customTitle);
    }
}
