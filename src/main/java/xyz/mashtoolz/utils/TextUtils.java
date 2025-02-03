package xyz.mashtoolz.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import xyz.mashtoolz.config.PBTW;

import java.util.regex.Matcher;

public class TextUtils {

    public static Text format(String... parts) {
        return format(String.join("", parts));
    }

    public static Text format(String input) {
        // Updated regex pattern to handle gradient tags and formatting codes properly
        Matcher matcher = PBTW.COLOR_PATTERN.matcher(input);

        MutableText result = Text.literal("");
        int lastEnd = 0; // Tracks the end of the last match
        Style currentStyle = Style.EMPTY; // To keep track of active styles (color or formatting)

        while (matcher.find()) {
            // Append text before the match
            if (matcher.start() > lastEnd) {
                String plainText = input.substring(lastEnd, matcher.start());
                result.append(Text.literal(plainText).setStyle(currentStyle));
            }

            // Handle gradient tags <gradient:#F4C4F3:#FC67FA>text</gradient>
            if (matcher.group(1) != null && matcher.group(2) != null) {
                String startColorStr = matcher.group(1);
                String endColorStr = matcher.group(2);
                String gradientText = matcher.group(3);

                // Convert start and end color strings to TextColor objects
                TextColor startColor = TextColor.fromRgb(Integer.parseInt(startColorStr.substring(1), 16)); // Remove '#' and parse
                TextColor endColor = TextColor.fromRgb(Integer.parseInt(endColorStr.substring(1), 16)); // Remove '#' and parse

                // Preserve the current formatting while applying the gradient
                result.append(createGradientText(gradientText, startColor, endColor).setStyle(currentStyle));
            }
            // Handle §x formatting codes
            else if (matcher.group(4) != null) {
                char formattingCode = matcher.group(4).charAt(0);
                Formatting formatting = Formatting.byCode(formattingCode);
                if (formatting != null) {
                    if (formatting == Formatting.RESET) {
                        currentStyle = Style.EMPTY; // Reset all styles
                    } else {
                        currentStyle = currentStyle.withFormatting(formatting); // Apply new formatting
                    }
                }
            }
            // Handle hex color like <#FF0000>
            else if (matcher.group(5) != null) {
                String hexColorStr = matcher.group(5);
                TextColor hexColor = TextColor.fromRgb(Integer.parseInt(hexColorStr.substring(2, 8), 16)); // Extract hex and convert
                currentStyle = currentStyle.withColor(hexColor); // Apply the hex color
            }
            // Handle regular plain text (text not in any tags)
            else if (matcher.group(6) != null) {
                String plainText = matcher.group(6);
                result.append(Text.literal(plainText).setStyle(currentStyle));
            }

            lastEnd = matcher.end(); // Update the last match end
        }

        // Append the rest of the text after the last match
        if (lastEnd < input.length()) {
            String remainingText = input.substring(lastEnd);
            result.append(Text.literal(remainingText).setStyle(currentStyle));
        }

        return result;
    }

    private static MutableText createGradientText(String text, TextColor startColor, TextColor endColor) {
        MutableText gradientText = Text.literal("");
        int textLength = text.length();

        for (int i = 0; i < textLength; i++) {
            float ratio = (float) i / (textLength - 1);
            int blendedColor = ColorUtils.blend(startColor.getRgb(), endColor.getRgb(), ratio);
            gradientText.append(Text.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(blendedColor))));
        }

        return gradientText;
    }

    public static String escapeStringToUnicode(String input, boolean removeUnicode) {
        StringBuilder builder = new StringBuilder(input.length());
        for (char ch : input.toCharArray()) {
            if (ch < 128)
                builder.append(ch);
            else if (!removeUnicode)
                builder.append("\\u").append(String.format("%04x", (int) ch));
        }
        return builder.toString();
    }
}
