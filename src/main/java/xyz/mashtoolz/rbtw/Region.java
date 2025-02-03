package xyz.mashtoolz.rbtw;

import net.minecraft.util.math.BlockPos;

public class Region {

    private final BlockPos min;
    private final BlockPos max;

    public Region(BlockPos min, BlockPos max) {
        this.min = min;
        this.max = max;
    }

    public boolean isInside(BlockPos blockPos) {
        return blockPos.getX() >= min.getX() - 50 && blockPos.getX() <= max.getX() + 50 &&
                blockPos.getY() >= min.getY() - 50 && blockPos.getY() <= max.getY() + 50 &&
                blockPos.getZ() >= min.getZ() - 50 && blockPos.getZ() <= max.getZ() + 50;
    }
}
