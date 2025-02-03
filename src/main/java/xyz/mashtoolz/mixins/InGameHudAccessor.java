package xyz.mashtoolz.mixins;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor("title")
    Text getTitle();

    @Accessor("subtitle")
    Text getSubtitle();

    @Accessor("overlayMessage")
    Text getOverlayMessage();

    @Accessor("overlayMessage")
    void setOverlayMessage(Text overlayMessage);

}
