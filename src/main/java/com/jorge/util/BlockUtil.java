package com.jorge.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class BlockUtil {
    static final Minecraft mc = Minecraft.getInstance();
    public static BlockPos below() {
        return mc.player.blockPosition().below();
    }
    public static boolean isAir(BlockPos p) {
        return mc.level.getBlockState(p).isAir();
    }
    public static Vec3 center(BlockPos p) {
        return new Vec3(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5);
    }
}
