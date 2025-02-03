package xyz.mashtoolz.config;

import java.util.regex.Pattern;

public class PBTW {

    public static final Pattern SHORT_HAND = Pattern.compile("^([\\d.]+)([kmbtKMBT]?)$");

    public static final Pattern OXYGEN_BAR = Pattern.compile("● OXYGEN (\\d+) ●");
    public static final Pattern TABLIST_FOOTER = Pattern.compile("⛃ (\\S+)\\$.+𐄷 \\d+ Items \\((\\d+)\\)");
    public static final Pattern UPGRADE_LEVEL = Pattern.compile("\\((\\d+)/(\\d+)\\)");
    public static final Pattern ALBERTO_LEVEL = Pattern.compile("(\\d+)/(\\d)+");
    public static final Pattern MRMONEY_LEVEL = Pattern.compile("LEVEL (\\d+)");
    public static final Pattern MRMONEY_XP = Pattern.compile("(\\d+) / (\\d+) XP");
    public static final Pattern COLOR_PATTERN = Pattern.compile("<gradient:(#[a-fA-F0-9]{6}):(#[a-fA-F0-9]{6})>(.*?)</gradient>|" + "§([a-zA-Z0-9])|" + "(<#[a-fA-F0-9]{6}>)|([^§<]+)");

    public static final Pattern NPC_CURRENCY = Pattern.compile("(\\d+)");

    public static final Pattern ANCIENT_MARKS = Pattern.compile("\\+ (\\d+)₴");
    public static final Pattern FISH_POINTS = Pattern.compile("\\+(\\d+) Ⓕ");
    public static final Pattern MUSEUM_CURRENCY = Pattern.compile("\\+(\\d+)♯");
    //
    public static final Pattern PRESTIGE = Pattern.compile("\\[ \\+(\\d+) \\+1 \\+1 ]");
    public static final Pattern SUPERPRESTIGE = Pattern.compile("\\[ \\+(\\d+) \\+1 \\+1 \\+1 ]");
    public static final Pattern REBIRTH = Pattern.compile("\\[ \\+(\\d+) \\+1 \\+1 \\+1 \\+1 ]");
    public static final Pattern APOTHEOSIS = Pattern.compile("\\[ \\+(\\d+) \\+1 \\+1 \\+1 \\+1 \\+1 ]");

}
