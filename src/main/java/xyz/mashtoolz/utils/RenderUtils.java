package xyz.mashtoolz.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import xyz.mashtoolz.RBTW;

public class RenderUtils {

    private static DrawContext context;
    private static TextRenderer textRenderer;

    public static void setContext(DrawContext context, TextRenderer textRenderer) {
        RenderUtils.context = context;
        RenderUtils.textRenderer = textRenderer;
    }

    public static void drawText(Text text, int x, int y) {
        if (context == null || textRenderer == null) return;
        int textWidth = RBTW.getClient().textRenderer.getWidth(text);
        context.drawTextWithShadow(textRenderer, text, -textWidth / 2 + x, y, 0xFFFFFF);
    }

    /**
     * Scales down, translates, and performs an operation, then scales and translates back.
     *
     * @param matrices   The matrix stack to manipulate.
     * @param scale      The scaling factor.
     * @param translateX The X translation factor (before scaling).
     * @param translateY The Y translation factor (before scaling).
     * @param operation  A lambda or functional interface representing the drawing operation.
     */
    public static void scaledTranslate(MatrixStack matrices, float scale, float translateX, float translateY, Runnable operation) {
        // Scale down
        matrices.scale(scale, scale, scale);

        // Translate to the desired position
        matrices.translate(translateX / scale, translateY / scale, 0);

        // Perform the operation (e.g., draw text)
        operation.run();

        // Translate back
        matrices.translate(-translateX / scale, -translateY / scale, 0);

        // Scale back to normal
        matrices.scale(1f / scale, 1f / scale, 1f / scale);
    }
}
