package xyz.mashtoolz;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;
import xyz.mashtoolz.config.CBTW;
import xyz.mashtoolz.config.Keybinds;
import xyz.mashtoolz.config.TBTW;
import xyz.mashtoolz.handlers.RenderHandler;
import xyz.mashtoolz.rbtw.PlayerStats;
import xyz.mashtoolz.utils.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RBTW implements ClientModInitializer {

    public static RBTW instance;
    private static MinecraftClient client;
    private static ConfigHolder<CBTW> config;
    private static PlayerStats playerStats;

    @Override
    public void onInitializeClient() {

        instance = this;
        client = MinecraftClient.getInstance();

        AutoConfig.register(CBTW.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CBTW.class);

        Keybinds.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client == null || client.player == null || client.world == null || client.getNetworkHandler() == null) return;

            Keybinds.update();

            if (!TBTW.inRBTW && inRBTW()) {
                TBTW.inRBTW = true;
            } else if (TBTW.inRBTW && !inRBTW()) {
                TBTW.inRBTW = false;
                playerStats = null;
            }

            if (!TBTW.inRBTW) return;
//
//            if (TBTW.lastMenu != null && TBTW.lastMenu.equals("Profiles")) {
//                TBTW.lastMenu = null;
//                boolean standingOnSpawnpoint = client.player.getBlockPos().equals(TBTW.SPAWN) || client.player.getBlockPos().equals(TBTW.DEV_SPAWN);
//                if (standingOnSpawnpoint) {
//                    getModstats();
//                    return;
//                }
//            }

            if (playerStats != null)
                playerStats.update();
        });

        HudRenderCallback.EVENT.register(RenderHandler::onHudRender);
        WorldRenderEvents.AFTER_ENTITIES.register(RenderHandler::renderWorld);
    }

    public static boolean inRBTW() {
        if (client == null || client.player == null || client.world == null || client.world.getScoreboard() == null) return false;
        return TBTW.PLOT.isInside(client.player.getBlockPos()) || TBTW.DEV_PLOT.isInside(client.player.getBlockPos());
    }

    public static void getModstats() {
        Objects.requireNonNull(client.getNetworkHandler()).sendChatMessage("@modstats");
    }

    public static MinecraftClient getClient() {
        return client;
    }

    public static ClientPlayerEntity getPlayer() {
        if (client == null || client.player == null) return null;
        return client.player;
    }

    public static CBTW getConfig() {
        return config.get();
    }

    public static void setPlayerStats(PlayerStats playerStats) {
        RBTW.playerStats = playerStats;
    }

    public static PlayerStats getPlayerStats() {
        return playerStats;
    }

    public static Map<Integer, Text> getScores(Scoreboard scoreboard) {
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        Map<Integer, Text> scores = new LinkedHashMap<>();
        for (ScoreboardEntry entry : scoreboard.getScoreboardEntries(objective)) {
            Team team = scoreboard.getScoreHolderTeam(entry.owner());
            scores.put(entry.value(), Team.decorateName(team, entry.name()));
        }
        return scores;
    }

    public static void log(String message, boolean ingame) {
        if (client == null || client.player == null) {
            log(message);
            return;
        }
        client.player.sendMessage(TextUtils.format(message), false);
    }

    public static void log(String message) {
        System.out.println("[RBTW] " + message);
    }

    public static void error(String message) {
        System.err.println("[RBTW] " + message);
    }
}
