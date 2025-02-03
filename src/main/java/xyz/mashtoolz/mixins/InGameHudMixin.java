package xyz.mashtoolz.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mashtoolz.RBTW;
import xyz.mashtoolz.config.CBTW;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void RBTW_renderScoreboardSidebar(DrawContext drawContext, ScoreboardObjective objective, CallbackInfo ci) {
        CBTW config = RBTW.getConfig();
        if (RBTW.inRBTW() && config.general.hideScoreboard)
            ci.cancel();
    }
}
