package xyz.mashtoolz.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class DrawBox {

    public static final HashMap<InteractionEntity, Integer> loot = new HashMap<>();

    public static void render(MatrixStack matrixStack, MinecraftClient client) {

        if (client.world == null || client.player == null || loot.isEmpty()) return;

        final Camera camera = client.gameRenderer.getCamera();
        final Vec3d cameraPos = camera.getPos();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        matrixStack.push();

        final Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        RenderSystem.enableDepthTest();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(3f);

        for (Map.Entry<InteractionEntity, Integer> s : loot.entrySet()) {
            float width = s.getKey().getInteractionWidth();
            float height = s.getKey().getInteractionHeight();
            final Vec3d pos = s.getKey().getPos().add(-width / 2, 0, -width / 2);
            final int color = s.getValue();
            final double x = pos.getX() - cameraPos.x;
            final double y = pos.getY() - cameraPos.y;
            final double z = pos.getZ() - cameraPos.z;
            final Box bb = new Box(x, y, z, x + width, y + height, z + width);
            drawOutlineBox(matrix, bufferBuilder, bb, color);
        }

        bufferBuilder.color(Colors.RED);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrixStack.pop();

        loot.clear();
    }

    private static void drawOutlineBox(Matrix4f matrix, BufferBuilder bufferBuilder, Box bb, int color) {
        final Vector3f normal = new Vector3f(0, 1, 0);

        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.minZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.maxZ, bb.minX, bb.minY, bb.maxZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.minY, bb.minZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.minX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.maxX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.maxX, bb.maxY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.minX, bb.maxY, bb.maxZ, bb.minX, bb.maxY, bb.minZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.minZ, bb.minX, bb.maxY, bb.minZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.maxZ, bb.maxX, bb.maxY, bb.maxZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, color, normal);
    }

    private static void drawLine(BufferBuilder bufferBuilder, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, int color, Vector3f normal) {
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(color).normal(normal.x, normal.y, normal.z);
        bufferBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(color).normal(normal.x, normal.y, normal.z);
    }

    public static void addLoot(InteractionEntity entity, int color) {
        loot.put(entity, color);
    }
}