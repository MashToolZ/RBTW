package xyz.mashtoolz.handlers;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.world.World;
import xyz.mashtoolz.RBTW;
import xyz.mashtoolz.config.CBTW;
import xyz.mashtoolz.config.TBTW;
import xyz.mashtoolz.enums.Color;
import xyz.mashtoolz.interfaces.EntityInterface;
import xyz.mashtoolz.mixins.BossBarHudAccessor;
import xyz.mashtoolz.rbtw.ClickerLevels;
import xyz.mashtoolz.rbtw.Layer;
import xyz.mashtoolz.rbtw.PlayerStats;
import xyz.mashtoolz.rbtw.Shop;
import xyz.mashtoolz.render.DrawBox;
import xyz.mashtoolz.utils.ColorUtils;
import xyz.mashtoolz.utils.NumberUtils;
import xyz.mashtoolz.utils.RenderUtils;
import xyz.mashtoolz.utils.TextUtils;

import java.util.*;

public class RenderHandler {

    private static final MinecraftClient client = RBTW.getClient();

    public static void onHudRender(DrawContext context, RenderTickCounter ignoredTickCounter) {

        if (!RBTW.inRBTW()) return;

        PlayerStats playerStats = RBTW.getPlayerStats();
        if (playerStats == null) return;

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        int scaledWidth = context.getScaledWindowWidth();
        int scaledHeight = context.getScaledWindowHeight();

        // Get BossBars and calculate yOffset
        Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) client.inGameHud.getBossBarHud()).getBossBars();

        RenderUtils.setContext(context, client.textRenderer);

        renderPlayerStats(context, matrices, scaledWidth, scaledHeight, playerStats, bossBars);
        renderLayerDisplay(context, scaledWidth, scaledHeight, playerStats, bossBars);

        matrices.pop();
    }

    public static void updateSlotStacks(ScreenHandler handler) {
        if (client.player == null || client.getNetworkHandler() == null) return;

        Screen screen = client.currentScreen;
        if (screen == null) return;

        String title = screen.getTitle().getString();
        if (title.equals("Menu")) return;

        PlayerStats playerStats = RBTW.getPlayerStats();
        if (playerStats == null) return;

        playerStats.update(title, handler);
    }

    private static void renderPlayerStats(DrawContext context, MatrixStack matrices, int scaledWidth, int scaledHeight, PlayerStats playerStats, Map<UUID, ClientBossBar> bossBars) {

        int maxHeight = scaledHeight / 3;
        int yOffset = 4;
        for (int i = 0; i < bossBars.size(); i++) {
            yOffset += 19;
            if (yOffset > maxHeight) break;
        }

        int halfWidth = scaledWidth / 2;
        matrices.translate(halfWidth, 0, 0);

        CBTW config = RBTW.getConfig();

        Shop fisher = playerStats.getFisher();
        Shop archeologist = playerStats.getArcheologist();
        Shop museum = playerStats.getMuseum();

        boolean unlockedFishPoints = fisher.getCurrency() > 0;

        int xOffset1 = 30;
        int xOffset2 = 90;

        // LAYER 1 - Prestige Tags
        boolean displayAD = playerStats.getAD() > 0;
        boolean displayToe = displayAD || playerStats.getToe() > 0;
        boolean displayHeureka = displayToe || playerStats.getHeureka() != 0;
        boolean displayResearch = displayToe || displayHeureka || playerStats.getResearch() > 0;
        Text prestigeTags = TextUtils.format(
                Color.AD.getHex(), displayAD ? ("ᛶ" + NumberUtils.toScript(playerStats.getAD(), false)) : "",
                Color.TOE.getHex(), displayToe ? (" ∀" + NumberUtils.toScript(playerStats.getToe(), true)) : "",
                Color.HEUREKA.getHex(), displayHeureka ? (" 🧪" + NumberUtils.toScript(playerStats.getHeureka(), false)) : "",
                Color.RESEARCH.getHex(), displayResearch ? " R" + NumberUtils.toScript(playerStats.getResearch(), true) : "",
                TBTW.researchCost > 0 ? " §f» " + (playerStats.getMoney() >= TBTW.researchCost ? "§a" : (playerStats.getMoney() + playerStats.getLootMoney()) >= TBTW.researchCost ? "§e" : "§c") + NumberUtils.compact(TBTW.researchCost) : ""
        );
        if (config.general.toggles.showPrestigeTags) {
            RenderUtils.drawText(prestigeTags, 0, yOffset);
            yOffset += 15;
        }

        // LAYER 2 - Clicker Levels
        ClickerLevels CL = playerStats.getClicker().getLevels();
        if (config.general.toggles.showClickerLevels && (CL.getApotheosis() != 0 || CL.getRebirth() != 0 || CL.getSuperprestige() != 0 || CL.getPrestige() != 0)) {
            String OPEN = Color.CL_BRACKET.getHex() + "[";
            String CLOSE = Color.CL_BRACKET.getHex() + "]";
            Text clickerLevels = TextUtils.format(OPEN + Color.APOTHEOSIS.getHex() + "+" + CL.getApotheosis() + CLOSE, OPEN + Color.REBIRTH.getHex() + "+" + CL.getRebirth() + CLOSE, OPEN + Color.SUPERPRESTIGE.getHex() + "+" + CL.getSuperprestige() + CLOSE, OPEN + Color.PRESTIGE.getHex() + "+" + CL.getPrestige() + CLOSE);
            RenderUtils.drawText(clickerLevels, 0, yOffset);
            yOffset += 15;
        }

        if (config.general.toggles.showTotalMultipliers) {
            // LAYER 3 - FishPoints Multiplier
            if (unlockedFishPoints && playerStats.getFishPointsMultiplier() > 1.0)
                RenderUtils.scaledTranslate(matrices, 0.75f, xOffset2, yOffset, () -> {
                    Text fishPointsMultiplier = TextUtils.format(Color.MULTIPLIER.getHex() + NumberUtils.compact(playerStats.getFishPointsMultiplier()) + "x   ");
                    RenderUtils.drawText(fishPointsMultiplier, 0, 0);
                });
            // LAYER 3 - Money Multiplier
            if (config.general.toggles.showMoney && playerStats.getMoneyMultiplier() > 1.0)
                RenderUtils.scaledTranslate(matrices, 0.75f, xOffset1, yOffset, () -> {
                    Text moneyMultiplier = TextUtils.format("   " + Color.MULTIPLIER.getHex() + NumberUtils.compact(playerStats.getMoneyMultiplier()) + "x");
                    RenderUtils.drawText(moneyMultiplier, 0, 0);
                });
            yOffset += 10;
        }

        // LAYER 4 - Total FishPoints
        if (config.general.toggles.showFishPoints && unlockedFishPoints) {
            Text totalFishPoints = TextUtils.format(Color.FISHPOINTS.getHex() + NumberUtils.compact(playerStats.getFisher().getCurrency() + playerStats.getLootFishPoints()) + " <#4A7FFF>Ⓕ");
            RenderUtils.drawText(totalFishPoints, xOffset2, yOffset);
        }

        // LAYER 4 - Total Money
        if (config.general.toggles.showMoney) {
            Text totalMoney = TextUtils.format("<#359635>$ " + Color.MONEY.getHex() + NumberUtils.compact(playerStats.getMoney() + playerStats.getLootMoney()));
            RenderUtils.drawText(totalMoney, xOffset1, yOffset);
        }

        // LAYER 4 - Total AncientMarks
        if (config.general.toggles.showAncientMarks && archeologist.getCurrency() != 0) {
            Text totalAncientMarks = TextUtils.format(Color.ANCIENT_MARKS.getHex() + NumberUtils.compact(archeologist.getCurrency() + playerStats.getLootAncientMarks()) + " <#8A8A57>₴");
            RenderUtils.drawText(totalAncientMarks, -xOffset1, yOffset);
        }

        // LAYER 4 - Total MuseumCurrency
        if (config.general.toggles.showMuseumCurrency && museum.getCurrency() != 0) {
            Text totalMuseumCurrency = TextUtils.format(Color.MUSEUM_CURRENCY.getHex() + NumberUtils.compact(museum.getCurrency()) + " <#91883C>♯");
            RenderUtils.drawText(totalMuseumCurrency, -xOffset2, yOffset);
        }

        yOffset += 15;

        // LAYER 5 - Loot FishPoints
        if (config.general.toggles.showFishPoints && config.general.toggles.showLootValues && unlockedFishPoints && playerStats.getLootFishPoints() != 0) {
            Text lootFishPoints = TextUtils.format("<#FFD769>" + NumberUtils.compact(playerStats.getLootFishPoints()) + " <#FCBA03>Ⓕ");
            RenderUtils.drawText(lootFishPoints, xOffset2, yOffset);
        }

        // LAYER 5 - Loot Money
        if (config.general.toggles.showMoney && config.general.toggles.showLootValues && playerStats.getLootMoney() != 0) {
            Text lootMoney = TextUtils.format("<#FCBA03>$ <#FFD769>" + NumberUtils.compact(playerStats.getLootMoney()));
            RenderUtils.drawText(lootMoney, xOffset1, yOffset);
        }

        // LAYER 5 - Loot AncientMarks
        if (config.general.toggles.showAncientMarks && config.general.toggles.showLootValues && archeologist.getCurrency() != 0 && playerStats.getLootAncientMarks() != 0) {
            Text lootAncientMarks = TextUtils.format("<#FFD769>" + NumberUtils.compact(playerStats.getLootAncientMarks()) + " <#FCBA03>₴");
            RenderUtils.drawText(lootAncientMarks, -xOffset1, yOffset);
        }

        // LAYER 5 - Swimsuit Tier
//        RenderUtils.drawText(TextUtils.format("§eSwimsuit Tier: ", "§6" + playerStats.getSwimsuit().getTier()), 0, yOffset + 15);

        if (config.general.toggles.showMultiplierList)
            RenderUtils.scaledTranslate(matrices, 0.75f, -halfWidth + 7, 0, () -> {
                drawMoneyMultipliers(context, playerStats);
            });

        matrices.translate(-halfWidth, 0, 0);
    }

    private static void drawMoneyMultipliers(DrawContext context, PlayerStats playerStats) {

        // Draw Multipliers
        Map<String, Double> multipliers = playerStats.getMoneyMultipliers();
        if (multipliers.isEmpty()) return;

        // Calculate the maximum width of the multiplier numbers (including "x")
        int maxNumberWidth = 0;
        for (String multiplierName : multipliers.keySet()) {
            String multiplierNumber = NumberUtils.compact(multipliers.get(multiplierName)) + "x";
            int numberWidth = client.textRenderer.getWidth(TextUtils.format(Color.MULTIPLIER.getHex() + multiplierNumber));
            maxNumberWidth = Math.max(maxNumberWidth, numberWidth);
        }

        int lineSpacing = 15;

        // Check index of last multiplier over 1
        int lastMultiplierIndex = 0;
        for (int i = 0; i < multipliers.size(); i++) {
            String multiplierName = multipliers.keySet().toArray(new String[0])[i];
            double multiplier = multipliers.get(multiplierName);
            if (multiplier <= 1.0 && !multiplierName.equals("Tradeoff")) continue;
            lastMultiplierIndex = i;
        }

        for (int i = 0; i < multipliers.size(); i++) {
            String multiplierName = multipliers.keySet().toArray(new String[0])[i];
            double multiplier = multipliers.get(multiplierName);
            if (i > lastMultiplierIndex) continue;

            // Prepare the multiplier number with "x"
            String multiplierNumber = NumberUtils.compact(multiplier) + "x";
            Color multiplierColor = Color.UNKNOWN;
            try {
                multiplierColor = Color.getByName(multiplierName.replaceAll("[ ?]", "_").toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }

            String color = String.format("<#%06X>", ColorUtils.darkenColor(multiplierColor.getInt(), 0.8f));

            // OddToe Strikethrough
            String strikeOddToe = (playerStats.getToe() % 2 == 0 && multiplierName.equals("Odd Toe")) ? "§m" : "";

            Text numberText = TextUtils.format(strikeOddToe + multiplierColor.getHex() + multiplierNumber);
            int numberWidth = client.textRenderer.getWidth(numberText);

            // Prepare the multiplier name
            Text nameText = TextUtils.format(strikeOddToe + color + multiplierName);

            // Calculate positions
            int numberX = maxNumberWidth - numberWidth;
            int nameX = numberX + numberWidth + 5;
            int textY = 10 + (i * lineSpacing);

            // Draw the number and the name separately
            context.drawTextWithShadow(client.textRenderer, numberText, numberX, textY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, nameText, nameX, textY, 0xFFFFFF);
        }
    }

    private static void renderLayerDisplay(DrawContext context, int scaledWidth, int scaledHeight, PlayerStats playerStats, Map<UUID, ClientBossBar> bossBars) {

        ClientPlayerEntity player = client.player;
        if (player == null) return;

        boolean isSwimming = player.isSubmergedInWater();
        if (!isSwimming || bossBars.isEmpty()) return;

        int layerMin = Layer.min();
        int layerMax = Layer.max();

        int width = 38;

        int startX = scaledWidth - 10 - width;
        int startY = scaledHeight - layerMax;

        int endX = startX + width;
        int endY = scaledHeight - 10;

        boolean isAlternate = TBTW.ALTERNATE.isInside(player.getBlockPos()) || TBTW.DEV_ALTERNATE.isInside(player.getBlockPos());

        Layer Surface = Layer.SURFACE;
        Layer Layer1 = isAlternate ? Layer.RED_SAND : Layer.SAND;
        Layer Layer2 = isAlternate ? Layer.DEEPSLATE : Layer.STONE;
        Layer Layer3 = isAlternate ? Layer.THE_END : Layer.NETHER;

        int Surface_min = NumberUtils.map(Surface.min, layerMax, layerMin, startY, endY).intValue();
        int Layer1_min = NumberUtils.map(Layer1.min, layerMax, layerMin, startY, endY).intValue();
        int Layer2_min = NumberUtils.map(Layer2.min, layerMax, layerMin, startY, endY).intValue();
        int Layer3_min = NumberUtils.map(Layer3.min, layerMax, layerMin, startY, endY).intValue();

        int playerY = NumberUtils.map(player.getY(), Layer1.max, Layer3.min, startY, endY).intValue();
        Layer currentLayer = Layer.getLayer(player);

        RenderUtils.drawText(TextUtils.format(currentLayer.textColor() + currentLayer.name), startX + width / 2, startY - 16);

        // Draw Background
        context.fill(startX - 1, startY - 1, endX + 1, endY + 1, ColorUtils.hex2Int("#191919", 64));

        // Draw Layers
        context.fill(startX, Surface_min, endX, Layer1_min, Layer1.fillColor());
        context.fill(startX, Layer1_min, endX, Layer2_min, Layer2.fillColor());
        context.fill(startX, Layer2_min, endX, Layer3_min, Layer3.fillColor());

        context.fill(startX - 1, playerY, endX + 1, playerY + 4, 0xC0000000);
        context.fill(startX, playerY + 1, endX, playerY + 3, currentLayer.fillColor());
    }

    public static void renderWorld(WorldRenderContext context) {

        if (!TBTW.inRBTW) return;

        ClientPlayerEntity player = client.player;
        if (player == null) return;

        World world = client.world;
        if (world == null) return;

        try {
            CBTW.General general = RBTW.getConfig().general;

            Iterable<Entity> entities = player.clientWorld.getEntities();

            List<InteractionEntity> interactions = new ArrayList<>();
            List<VillagerEntity> villagers = new ArrayList<>();

            for (Entity entity : entities) {
                if (!TBTW.PLOT.isInside(entity.getBlockPos()) && !TBTW.DEV_PLOT.isInside(entity.getBlockPos())) continue;

                if (entity instanceof InteractionEntity _entity) {
                    if (_entity.getY() < 210 && player.canInteractWithEntity(_entity, 0))
                        interactions.add(_entity);
                } else if (entity instanceof VillagerEntity _entity) {
                    villagers.add(_entity);
                }
            }

            if (general.highlights.lootInRange) {
                for (InteractionEntity entity : interactions)
                    DrawBox.addLoot(entity, 0xFFFFFFFF);
                DrawBox.render(context.matrixStack(), client);
            } else if (!DrawBox.loot.isEmpty())
                DrawBox.loot.clear();

            for (VillagerEntity villager : villagers) {
                TextColor textColor = Optional.ofNullable(villager.getName()).map(Text::getSiblings).filter(siblings -> !siblings.isEmpty()).map(List::getFirst).map(Text::getStyle).map(Style::getColor).orElse(TextColor.fromRgb(0xD1D1D1));
                EntityInterface entity = (EntityInterface) villager;
                int color = ColorUtils.darkenColor(textColor.getRgb(), player.canInteractWithEntity(villager, 0) ? 1.0f : 0.5f);
                entity.RBTW_setForceGlowing((player.getY() < 210 || player.distanceTo(villager) > 24 || !general.highlights.villagersInRange) ? 1 : player.canSee(villager) ? 2 : 0);
                entity.RBTW_setGlowingColor(color);
            }

        } catch (Exception ignored) {
        }
    }
}