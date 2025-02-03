package xyz.mashtoolz.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.mashtoolz.RBTW;
import xyz.mashtoolz.config.PBTW;
import xyz.mashtoolz.config.TBTW;
import xyz.mashtoolz.rbtw.Clicker;
import xyz.mashtoolz.rbtw.ClickerLevels;
import xyz.mashtoolz.rbtw.PlayerStats;
import xyz.mashtoolz.rbtw.Upgrades;

import java.util.regex.Matcher;

public class ChatHandler {

    public static void addMessage(Text text, CallbackInfo ci) {

        MinecraftClient client = RBTW.getClient();
        if (client == null || client.getNetworkHandler() == null) return;

        String message = text.getString();
        if (message.startsWith("Extra Mod Info:"))
            ci.cancel();

        if (message.startsWith("@msu")) {
            ci.cancel();
            if (message.equals("@msu switchProfile")) {
                RBTW.getModstats();
                return;
            }

            PlayerStats.update(message);
        } else if (message.startsWith("\n@ms")) {
            ci.cancel();
            RBTW.log("§7[<#3B8DED>RBTW§7] §eProfile loaded", true);
            RBTW.setPlayerStats(PlayerStats.from(message));
        }

        if (message.equals("Done!") && TBTW.inRBTW) {
            RBTW.getModstats();
        }

        PlayerStats playerStats = RBTW.getPlayerStats();
        if (playerStats == null) return;

        switch (message) {

            // Swimsuit
            case ">> Diving Gear upgraded!" -> {
                playerStats.setSwimsuit(playerStats.getSwimsuit().getTier() + 1);
                return;
            }
        }

        // FishPoints Parsing
        Matcher fishPointsMatcher = PBTW.FISH_POINTS.matcher(message);
        if (fishPointsMatcher.find()) {
            playerStats.getFisher().addCurrency(Integer.parseInt(fishPointsMatcher.group(1)));
            return;
        }

        // Ancient Marks Parsing
        Matcher ancientMarksMatcher = PBTW.ANCIENT_MARKS.matcher(message);
        if (ancientMarksMatcher.find()) {
            playerStats.getArcheologist().addCurrency(Integer.parseInt(ancientMarksMatcher.group(1)));
            return;
        }

        // Museum Currency Parsing
        Matcher museumCurrencyMatcher = PBTW.MUSEUM_CURRENCY.matcher(message);
        if (museumCurrencyMatcher.find()) {
            playerStats.getMuseum().addCurrency(Integer.parseInt(museumCurrencyMatcher.group(1)));
            return;
        }

        // Clicker Level Parsing
        Clicker clicker = playerStats.getClicker();
        Upgrades rebirthUpgrades = clicker.getRebirthUpgrades();
        Upgrades apotheosisUpgrades = clicker.getApotheosisUpgrades();

        ClickerLevels CL = playerStats.getClicker().getLevels();
        Matcher prestigeMatcher = PBTW.PRESTIGE.matcher(message);
        Matcher superPrestigeMatcher = PBTW.SUPERPRESTIGE.matcher(message);
        Matcher rebirthMatcher = PBTW.REBIRTH.matcher(message);
        Matcher apotheosisMatcher = PBTW.APOTHEOSIS.matcher(message);

        if (prestigeMatcher.find()) {
            ci.cancel();

            CL.setPrestige(CL.getPrestige() + Integer.parseInt(prestigeMatcher.group(1)));

        } else if (superPrestigeMatcher.find()) {
            ci.cancel();

            CL.setPrestige(0);
            CL.setSuperprestige(CL.getSuperprestige() + Integer.parseInt(superPrestigeMatcher.group(1)));

        } else if (rebirthMatcher.find()) {
            ci.cancel();

            boolean hasVault = rebirthUpgrades.has(4);
            CL.setPrestige(hasVault ? 2 : 0);
            CL.setSuperprestige(hasVault ? 2 : 0);
            CL.setRebirth(CL.getRebirth() + Integer.parseInt(rebirthMatcher.group(1)));

        } else if (apotheosisMatcher.find()) {
            ci.cancel();

            boolean hasDivineInheritance = apotheosisUpgrades.has(2);
            CL.setPrestige(hasDivineInheritance ? 2 : 0);
            CL.setSuperprestige(hasDivineInheritance ? 2 : 0);
            CL.setRebirth(hasDivineInheritance ? 2 : 0);
            CL.setApotheosis(CL.getApotheosis() + Integer.parseInt(apotheosisMatcher.group(1)));

            boolean hasEternalBirthright = apotheosisUpgrades.has(8);
            if (!hasEternalBirthright)
                rebirthUpgrades.reset();

        }
    }
}
