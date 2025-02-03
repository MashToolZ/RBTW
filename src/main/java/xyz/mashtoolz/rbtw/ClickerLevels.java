package xyz.mashtoolz.rbtw;

public class ClickerLevels {

    private int prestige;
    private int superprestige;
    private int rebirth;
    private int apotheosis;

    public ClickerLevels(int[] levels) {
        this.prestige = levels[0];
        this.superprestige = levels[1];
        this.rebirth = levels[2];
        this.apotheosis = levels[3];
    }

    public int getPrestige() {
        return prestige;
    }

    public int getSuperprestige() {
        return superprestige;
    }

    public int getRebirth() {
        return rebirth;
    }

    public int getApotheosis() {
        return apotheosis;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public void setSuperprestige(int superprestige) {
        this.superprestige = superprestige;
    }

    public void setRebirth(int rebirth) {
        this.rebirth = rebirth;
    }

    public void setApotheosis(int apotheosis) {
        this.apotheosis = apotheosis;
    }
}
