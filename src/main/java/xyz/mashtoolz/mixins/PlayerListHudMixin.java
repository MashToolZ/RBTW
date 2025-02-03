package xyz.mashtoolz.mixins;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mashtoolz.RBTW;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void RBTW_onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().push();
        if (RBTW.inRBTW())
            context.getMatrices().translate(0, RBTW.getConfig().general.playerListHeightOffset, 0);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void RBTW_onRenderReturn(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        context.getMatrices().pop();
    }
}

