package xyz.mashtoolz.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemUtils {

    public static NbtCompound getPBV(ItemStack stack) {
        NbtComponent component = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if (component == null) return null;
        NbtCompound nbt = component.copyNbt();
        return nbt.getCompound("PublicBukkitValues");
    }
}
