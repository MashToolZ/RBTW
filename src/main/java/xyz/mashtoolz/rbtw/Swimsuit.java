package xyz.mashtoolz.rbtw;

import java.util.List;

public class Swimsuit {

    public static final Swimsuit TIER_0 = new Swimsuit(0, "AIR", "AIR", "SWIMMING TRUNKS", "AIR");
    public static final Swimsuit TIER_1 = new Swimsuit(1, "AIR", "NEOPRENE SUIT", "NEOPRENE TRUNKS", "FLIPPERS");
    public static final Swimsuit TIER_2 = new Swimsuit(2, "AIR", "NEOPRENE SUIT MK.2", "NEOPRENE TRUNKS MK.2", "FLIPPERS MK.2");
    public static final Swimsuit TIER_3 = new Swimsuit(3, "DIVING MASK", "NEOPRENE SUIT MK.2", "NEOPRENE TRUNKS MK.2", "FLIPPERS MK.2");
    public static final Swimsuit TIER_4 = new Swimsuit(4, "DIVING MASK", "DIVING SUIT", "NEOPRENE TRUNKS MK.2", "FLIPPERS MK.2");
    public static final Swimsuit TIER_5 = new Swimsuit(5, "DIVING MASK MK.2", "DIVING SUIT", "DIVING TRUNKS MK.2", "FLIPPERS MK.3");
    public static final Swimsuit TIER_6 = new Swimsuit(6, "ALTERNATE HELMET", "ALTERNATE SUIT", "ALTERNATE PANTS", "ALTERNATE FLIPPERS");

    public static final Swimsuit[] values = new Swimsuit[]{Swimsuit.TIER_0, Swimsuit.TIER_1, Swimsuit.TIER_2, Swimsuit.TIER_3, Swimsuit.TIER_4, Swimsuit.TIER_5, Swimsuit.TIER_6};

    private final int tier;
    private final String feet;
    private final String legs;
    private final String body;
    private final String head;

    private final double[] oxygenMultipliers = new double[]{1.0, 1.25, 1.35, 1.35, 1.5, 1.5, 1.6};
    private final double[] oxygenUsageMultipliers = new double[]{1.0, 1.0, 1.0, 0.8, 0.8, 0.75, 0.7};

    private Swimsuit(int tier, String head, String body, String legs, String feet) {
        this.tier = tier;
        this.head = head;
        this.body = body;
        this.legs = legs;
        this.feet = feet;
    }

    public int getTier() {
        return this.tier;
    }

    public static Swimsuit from(int tier) {
        if (tier < 0 || tier >= Swimsuit.values.length) return Swimsuit.TIER_0;
        return Swimsuit.values[tier];
    }

    public double getOxygenMultiplier() {
        if (this.tier < 0 || this.tier >= this.oxygenMultipliers.length) return 1.0;
        return this.oxygenMultipliers[this.tier];
    }

    public double getOxygenUsageMultiplier() {
        if (this.tier < 0 || this.tier >= this.oxygenUsageMultipliers.length) return 1.0;
        return this.oxygenUsageMultipliers[this.tier];
    }

    public static Swimsuit from(List<String> items) {
        for (Swimsuit other : Swimsuit.values)
            if (items.get(0).equals(other.head) && items.get(1).equals(other.body) && items.get(2).equals(other.legs) && items.get(3).equals(other.feet))
                return other;
        return Swimsuit.TIER_0;
    }
}