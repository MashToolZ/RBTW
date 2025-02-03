package xyz.mashtoolz.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rbtw")
public class CBTW implements ConfigData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public General general = new General();

    public static class General {

        @ConfigEntry.Gui.Tooltip
        public boolean hideScoreboard = true;

        @ConfigEntry.Gui.Tooltip
        public int playerListHeightOffset = 10;

        @ConfigEntry.Gui.CollapsibleObject()
        public Toggles toggles = new Toggles();

        public static class Toggles {

            @ConfigEntry.Gui.Tooltip
            public boolean showPrestigeTags = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showLootValues = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showMoney = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showFishPoints = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showMuseumCurrency = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showAncientMarks = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showClickerLevels = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showTotalMultipliers = true;

            @ConfigEntry.Gui.Tooltip
            public boolean showMultiplierList = false;
        }

        @ConfigEntry.Gui.CollapsibleObject()
        public Highlights highlights = new Highlights();

        public static class Highlights {
            @ConfigEntry.Gui.Tooltip
            public boolean lootInRange = true;

            @ConfigEntry.Gui.Tooltip
            public boolean villagersInRange = true;
        }
    }
}
