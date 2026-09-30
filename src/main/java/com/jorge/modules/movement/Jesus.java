package com.jorge.modules.movement;

import com.jorge.core.*;

public class Jesus extends HackModule {
    public Jesus() { super("Jesus", "Walk on water", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (mc.player.isInWater() && !mc.player.isCrouching()) {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.08, mc.player.getDeltaMovement().z);
        }
    }
}
