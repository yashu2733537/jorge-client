package com.jorge.modules.player;

import com.jorge.core.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.*;

public class AutoTool extends HackModule {
    public AutoTool() { super("AutoTool", "Picks best tool", Category.PLAYER); }
    @Override public void onTick() {
        if (mc.player == null || mc.hitResult == null) return;
        if (!(mc.hitResult instanceof net.minecraft.world.phys.BlockHitResult bhr)) return;
        if (!mc.options.keyAttack.isDown()) return;
        BlockState state = mc.level.getBlockState(bhr.getBlockPos());
        float best = 1f; int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.player.getInventory().getItem(i);
            float spd = s.getDestroySpeed(state);
            if (spd > best) { best = spd; slot = i; }
        }
        if (slot != -1) mc.player.getInventory().setSelectedSlot(slot);
    }
}
