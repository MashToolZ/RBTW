package xyz.mashtoolz.utils;

import xyz.mashtoolz.config.PBTW;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    public static final DecimalFormat COMPACT_FORMAT = new DecimalFormat("#,##0.###");
    public static final DecimalFormat FORMAT_1 = new DecimalFormat("#.0");
    public static final DecimalFormat FORMAT_2 = new DecimalFormat("#.00");
    public static final DecimalFormat FORMAT_3 = new DecimalFormat("#.000");

    public static double logN(double base, double value) {
        return Math.log(value) / Math.log(base);
    }

    public static String compact(double number) {
        String[] suffixes = {"", "K", "M", "B", "T"};

        if (number < 1000)
            return format(number);

        int magnitude = (int) Math.log10(number);
        int suffixIndex = magnitude / 3;

        if (suffixIndex > 4) suffixIndex = 4;

        double compactNumber = number / Math.pow(10, 3 * suffixIndex);

        return truncate(compactNumber) + suffixes[suffixIndex];
    }

    private static String truncate(double number) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(3, RoundingMode.DOWN);
        return bd.stripTrailingZeros().toPlainString();
    }

    private static String format(double number) {
        COMPACT_FORMAT.setMinimumFractionDigits(2);
        COMPACT_FORMAT.setMaximumFractionDigits(3);
        return COMPACT_FORMAT.format(number);
    }

    public static double parseCompact(String input) {

        if (input == null || input.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ignored) {
        }

        // Regular expression for matching shorthand numbers
        Pattern pattern = PBTW.SHORT_HAND;
        Matcher matcher = pattern.matcher(input.trim());

        if (matcher.matches()) {
            double number = Double.parseDouble(matcher.group(1));
            String suffix = matcher.group(2).toLowerCase();

            return switch (suffix) {
                case "k" -> number * 1e3;
                case "m" -> number * 1e6;
                case "b" -> number * 1e9;
                case "t" -> number * 1e12;
                default -> number; // No suffix means it's just the number
            };
        }

        return 0.0; // Return 0 if the input does not match
    }

    public static Number map(double value, double fromMin, double fromMax, double toMin, double toMax) {
        return (value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
    }

    // Maps for superscript and subscript
    private static final Map<Integer, Character> SUPER_SCRIPT_MAP = new HashMap<>();
    private static final Map<Integer, Character> SUB_SCRIPT_MAP = new HashMap<>();

    static {
        SUPER_SCRIPT_MAP.put(0, '⁰');
        SUB_SCRIPT_MAP.put(0, '₀');
        SUPER_SCRIPT_MAP.put(1, '¹');
        SUB_SCRIPT_MAP.put(1, '₁');
        SUPER_SCRIPT_MAP.put(2, '²');
        SUB_SCRIPT_MAP.put(2, '₂');
        SUPER_SCRIPT_MAP.put(3, '³');
        SUB_SCRIPT_MAP.put(3, '₃');
        SUPER_SCRIPT_MAP.put(4, '⁴');
        SUB_SCRIPT_MAP.put(4, '₄');
        SUPER_SCRIPT_MAP.put(5, '⁵');
        SUB_SCRIPT_MAP.put(5, '₅');
        SUPER_SCRIPT_MAP.put(6, '⁶');
        SUB_SCRIPT_MAP.put(6, '₆');
        SUPER_SCRIPT_MAP.put(7, '⁷');
        SUB_SCRIPT_MAP.put(7, '₇');
        SUPER_SCRIPT_MAP.put(8, '⁸');
        SUB_SCRIPT_MAP.put(8, '₈');
        SUPER_SCRIPT_MAP.put(9, '⁹');
        SUB_SCRIPT_MAP.put(9, '₉');
    }

    public static String toScript(int number, boolean upper) {

        Map<Integer, Character> map = upper ? SUPER_SCRIPT_MAP : SUB_SCRIPT_MAP;
        StringBuilder result = new StringBuilder();

        // Break the number into digits
        String numStr = Integer.toString(number);
        for (char digit : numStr.toCharArray()) {
            int num = Character.getNumericValue(digit);
            result.append(map.get(num));
        }

        return result.toString();
    }
}
