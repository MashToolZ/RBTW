package xyz.mashtoolz.rbtw;

public class Clicker extends Shop {

    private final ClickerLevels levels;
    private final Upgrades rebirthUpgrades;
    private final Upgrades apotheosisUpgrades;

    public Clicker(int[] levels) {
        super();
        this.levels = new ClickerLevels(levels);
        this.rebirthUpgrades = new Upgrades();
        this.apotheosisUpgrades = new Upgrades();
    }

    public ClickerLevels getLevels() {
        return levels;
    }

    public Upgrades getRebirthUpgrades() {
        return rebirthUpgrades;
    }

    public Upgrades getApotheosisUpgrades() {
        return apotheosisUpgrades;
    }
}
