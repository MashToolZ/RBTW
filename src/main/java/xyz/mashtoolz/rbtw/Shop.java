package xyz.mashtoolz.rbtw;

public class Shop {

    private double currency = 0;
    private final Upgrades upgrades;

    public Shop() {
        this.upgrades = new Upgrades();
    }

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public void addCurrency(double currency) {
        this.currency += currency;
    }

    public void subCurrency(double currency) {
        this.currency -= currency;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }
}
