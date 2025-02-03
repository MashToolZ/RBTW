package xyz.mashtoolz.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.mashtoolz.interfaces.EntityInterface;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {

    @Unique
    private int glowingColor = -1;

    @Unique
    private int forceGlowing = 1;

    @Override
    public void RBTW_setGlowingColor(int glowingColor) {
        this.glowingColor = glowingColor & 0xFFFFFF;
    }

    @Override
    public void RBTW_resetColor() {
        glowingColor = -1;
    }

    @Override
    public void RBTW_setForceGlowing(int glowing) {
        forceGlowing = glowing;
    }

    @Inject(method = "getTeamColorValue()I", cancellable = true, at = @At("HEAD"))
    public void RBTW_getTeamColorValue(CallbackInfoReturnable<Integer> ci) {
        if (glowingColor != -1) {
            ci.setReturnValue(glowingColor);
            ci.cancel();
        }
    }

    @Inject(method = "isGlowing", at = @At("RETURN"), cancellable = true)
    public void RBTW_isGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (forceGlowing == 0) {
            cir.setReturnValue(false);
        } else if (forceGlowing == 2) {
            cir.setReturnValue(true);
        }
    }
}