package com.jorge.modules.movement;

import com.jorge.core.*;

public class SafeWalk extends HackModule {
    public SafeWalk() { super("SafeWalk", "No falling off edges", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (mc.level.getBlockState(mc.player.blockPosition().below()).isAir() && mc.player.onGround()) {
            mc.player.setDeltaMovement(0, mc.player.getDeltaMovement().y, 0);
        }
    }
}
