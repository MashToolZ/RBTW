package xyz.mashtoolz.enums;

import java.util.HashMap;
import java.util.Map;

public class Color {

    private static final Map<String, Color> COLORS = new HashMap<>();

    public static final Color AD = new Color("AD", 0xB080FB);
    public static final Color TOE = new Color("TOE", 0xFBFB86);
    public static final Color HEUREKA = new Color("HEUREKA", 0x86C9FB);
    public static final Color RESEARCH = new Color("RESEARCH", 0x86A7C9);
    public static final Color CL_BRACKET = new Color("CL_BRACKET", 0x545454);
    public static final Color APOTHEOSIS = new Color("APOTHEOSIS", 0x54FBFB);
    public static final Color REBIRTH = new Color("REBIRTH", 0xFB5454);
    public static final Color SUPERPRESTIGE = new Color("SUPERPRESTIGE", 0xA700A7);
    public static final Color PRESTIGE = new Color("PRESTIGE", 0x54FB54);
    public static final Color MONEY = new Color("MONEY", 0x86FC86);
    public static final Color FISHPOINTS = new Color("FISHPOINTS", 0x86A8FB);
    public static final Color ANCIENT_MARKS = new Color("ANCIENT_MARKS", 0xA8A886);
    public static final Color MUSEUM_CURRENCY = new Color("MUSEUM_CURRENCY", 0xCDC05B);
    public static final Color MULTIPLIER = new Color("MULTIPLIER", 0xF4A3FF);


//    static {
//        COLORS.put("MONEY_MAKER", new Color("MONEY_MAKER", 0x86FC86));
//        COLORS.put("BREAKTHROUGHS", new Color("BREAKTHROUGHS", 0x86C9FB));
//        COLORS.put("MILESTONE_MULTIPLIER", new Color("MILESTONE_MULTIPLIER", 0xFBFBA8));
//        COLORS.put("LOADED_LOGARITHM", new Color("LOADED_LOGARITHM", 0x86A7FB));
//        COLORS.put("MILESTONE_IV", new Color("MILESTONE_IV", 0xFBC9A7));
//        COLORS.put("ODD_TOE", new Color("ODD_TOE", 0xFBCAA8));
//        COLORS.put("KNOWLEDGE_GAIN", new Color("KNOWLEDGE_GAIN", 0xFBA754));
//        COLORS.put("CRAZY_CLICKS", new Color("CRAZY_CLICKS", 0x54FB54));
//        COLORS.put("PROSPEROUS_PRESTIGES", new Color("PROSPEROUS_PRESTIGES", 0xFB54FB));
//        COLORS.put("ALTERNATE_DIMENSIONS", new Color("ALTERNATE_DIMENSIONS", 0xCDB0FB));
//        COLORS.put("EVEN_MORE_MONEY", new Color("EVEN_MORE_MONEY", 0xFBFB86));
//        COLORS.put("MORER_MONEY", new Color("MORER_MONEY", 0xB080FB));
//        COLORS.put("STACKS_OF_MONEY", new Color("STACKS_OF_MONEY", 0xB080FB));
//        COLORS.put("MORE_MONEY__", new Color("MORE_MONEY__", 0x86C9FB));
//        COLORS.put("FREEDOM", new Color("FREEDOM", 0xCDC05B));
//        COLORS.put("AWESOME_HEUREKA", new Color("AWESOME_HEUREKA", 0x86A7FB));
//        COLORS.put("AMAZING_MONEY", new Color("AMAZING_MONEY", 0xFBFBA7));
//        COLORS.put("SADNESS", new Color("SADNESS", 0xFB5454));
//        COLORS.put("TOETASTIC", new Color("TOETASTIC", 0xFBFBA8));
//        COLORS.put("SUPER_STATISTIC", new Color("SUPER_STATISTIC", 0x86FB86));
//        COLORS.put("AMAZING_APOTHEOSIS", new Color("AMAZING_APOTHEOSIS", 0x54FBFB));
//        COLORS.put("DIURNAL_RHYTHMS", new Color("DIURNAL_RHYTHMS", 0x545454));
//        COLORS.put("ALBERTO_MONEY", new Color("ALBERTO_MONEY", 0x86A7FB));
//        COLORS.put("TRADEOFF", new Color("TRADEOFF", 0x86A786));
//    }

    public static final Color UNKNOWN = new Color("UNKNOWN", 0x000000);

    private final String name;
    private final int color;

    private Color(String name, int color) {
        this.name = name;
        this.color = color;
        COLORS.put(name, this);
    }

    public String getHex() {
        return String.format("<#%06X>", color);
    }

    public int getInt() {
        return color;
    }

    public static Color getByName(String name) {
        Color color = getColors().get(name);
        return color != null ? color : UNKNOWN;
    }

    @Override
    public String toString() {
        return name + "(" + getHex() + ")";
    }

    public String getString() {
        return name;
    }

    public static Map<String, Color> getColors() {
        COLORS.clear();

        COLORS.put("MONEY_MAKER", new Color("MONEY_MAKER", 0x86FC86));
        COLORS.put("BREAKTHROUGHS", new Color("BREAKTHROUGHS", 0x86C9FB));
        COLORS.put("MILESTONE_MULTIPLIER", new Color("MILESTONE_MULTIPLIER", 0xFBFBA8));
        COLORS.put("LOADED_LOGARITHM", new Color("LOADED_LOGARITHM", 0x86A7FB));
        COLORS.put("MILESTONE_IV", new Color("MILESTONE_IV", 0xFBC9A7));
        COLORS.put("ODD_TOE", new Color("ODD_TOE", 0xFBCAA8));
        COLORS.put("KNOWLEDGE_GAIN", new Color("KNOWLEDGE_GAIN", 0xFBA754));
        COLORS.put("CRAZY_CLICKS", new Color("CRAZY_CLICKS", 0x54FB54));
        COLORS.put("PROSPEROUS_PRESTIGES", new Color("PROSPEROUS_PRESTIGES", 0xFB54FB));
        COLORS.put("ALTERNATE_DIMENSIONS", new Color("ALTERNATE_DIMENSIONS", 0xCDB0FB));
        COLORS.put("EVEN_MORE_MONEY", new Color("EVEN_MORE_MONEY", 0xFBFB86));
        COLORS.put("MORER_MONEY", new Color("MORER_MONEY", 0xB080FB));
        COLORS.put("STACKS_OF_MONEY", new Color("STACKS_OF_MONEY", 0xB080FB));
        COLORS.put("MORE_MONEY__", new Color("MORE_MONEY__", 0x86C9FB));
        COLORS.put("FREEDOM", new Color("FREEDOM", 0xCDC05B));
        COLORS.put("AWESOME_HEUREKA", new Color("AWESOME_HEUREKA", 0x86A7FB));
        COLORS.put("AMAZING_MONEY", new Color("AMAZING_MONEY", 0xFBFBA7));
        COLORS.put("SADNESS", new Color("SADNESS", 0xFB5454));
        COLORS.put("TOETASTIC", new Color("TOETASTIC", 0xFBFBA8));
        COLORS.put("SUPER_STATISTIC", new Color("SUPER_STATISTIC", 0x86FB86));
        COLORS.put("AMAZING_APOTHEOSIS", new Color("AMAZING_APOTHEOSIS", 0x54FBFB));
        COLORS.put("DIURNAL_RHYTHMS", new Color("DIURNAL_RHYTHMS", 0x545454));
        COLORS.put("ALBERTO_MONEY", new Color("ALBERTO_MONEY", 0x86A7FB));
        COLORS.put("MR._MONEY", new Color("MR._MONEY", 0xFBC986));
        COLORS.put("TRADEOFF", new Color("TRADEOFF", 0x86A786));

        return COLORS;
    }
}