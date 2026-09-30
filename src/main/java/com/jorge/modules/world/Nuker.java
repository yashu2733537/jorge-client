package com.jorge.modules.level;

import com.jorge.core.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Nuker extends HackModule {
    public final NumberSetting radius = add(new NumberSetting("Radius", 3.0, 1.0, 6.0, 0.5));
    public final BooleanSetting flat = add(new BooleanSetting("Flat", false));
    public Nuker() { super("Nuker", "Breaks blocks around you", Category.WORLD); }
    @Override public void onTick() {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;
        int r = radius.getInt();
        BlockPos pp = mc.player.blockPosition();
        for (int x = -r; x <= r; x++) for (int y = -r; y <= (flat.isOn() ? 0 : r); y++) for (int z = -r; z <= r; z++) {
            BlockPos p = pp.offset(x, y, z);
            if (mc.level.getBlockState(p).isAir()) continue;
            if (p.distSqr(pp) > r * r) continue;
            mc.gameMode.startDestroyBlock(p, Direction.UP);
            mc.player.swing(InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
            return;
        }
    }
}
