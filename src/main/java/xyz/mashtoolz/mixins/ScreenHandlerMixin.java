package xyz.mashtoolz.mixins;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mashtoolz.handlers.RenderHandler;

import java.util.List;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Inject(method = "updateSlotStacks", at = @At("TAIL"))
    public void RBTW_updateSlotStacks(int revision, List<ItemStack> stacks, ItemStack cursorStack, CallbackInfo ci) {
        RenderHandler.updateSlotStacks((ScreenHandler) (Object) this);
    }

    @Inject(method = "setStackInSlot", at = @At("TAIL"))
    public void RBTW_setStackInSlot(int slot, int revision, ItemStack stack, CallbackInfo ci) {
        RenderHandler.updateSlotStacks((ScreenHandler) (Object) this);
    }
}
