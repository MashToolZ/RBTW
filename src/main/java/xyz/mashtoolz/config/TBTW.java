package xyz.mashtoolz.config;

import net.minecraft.util.math.BlockPos;
import xyz.mashtoolz.rbtw.Region;

import java.util.HashMap;
import java.util.Map;

public class TBTW {

    // NORMAL
    public static final BlockPos SPAWN = new BlockPos(104150, 214, 4150);
    public static final Region PLOT = new Region(new BlockPos(104000, -10000, 4000), new BlockPos(105000, 10000, 5000));
    public static final Region ISLAND = new Region(new BlockPos(104000, -10000, 4000), new BlockPos(104300, 10000, 4300));
    public static final Region ALTERNATE = new Region(new BlockPos(104301, -10000, 4000), new BlockPos(104601, 10000, 4300));
    public static final Region RAFT = new Region(new BlockPos(104000, 50, 4302), new BlockPos(104049, 10000, 4350));

    // DEV
    public static final BlockPos DEV_SPAWN = new BlockPos(104150, 214, 6150);
    public static final Region DEV_PLOT = new Region(new BlockPos(104000, -10000, 6000), new BlockPos(105000, 10000, 7000));
    public static final Region DEV_ISLAND = new Region(new BlockPos(104000, -10000, 6000), new BlockPos(104300, 10000, 6300));
    public static final Region DEV_ALTERNATE = new Region(new BlockPos(104301, -10000, 6000), new BlockPos(104601, 10000, 6300));
    public static final Region DEV_RAFT = new Region(new BlockPos(104000, 50, 6302), new BlockPos(104049, 10000, 6350));

    public static boolean inRBTW = false;
    public static String lastMenu = null;
    public static double researchCost = 0;

    public static final Map<Integer, Integer> Upgrades = new HashMap<>();
}
