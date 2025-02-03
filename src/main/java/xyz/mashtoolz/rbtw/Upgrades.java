package xyz.mashtoolz.rbtw;

public class Upgrades {

    private int[] upgrades;

    public Upgrades() {
        this.upgrades = PlayerStats.EMPTY_INT_ARRAY;
    }

    public void set(int[] upgrades) {
        this.upgrades = upgrades;
    }

    public int[] get() {
        return upgrades;
    }

    public int get(int index) {
        if (index >= upgrades.length) return 0;
        return upgrades[index];
    }

    public boolean has(int index) {
        if (index >= upgrades.length) return false;
        return upgrades[index] >= 1;
    }

    public void reset() {
        this.upgrades = PlayerStats.EMPTY_INT_ARRAY;
    }

    public void reset(int from, int to) {
        for (int i = 0; i < upgrades.length; i++) {
            if (i < from || i > to) continue;
            upgrades[i] = 0;
        }
    }
}
