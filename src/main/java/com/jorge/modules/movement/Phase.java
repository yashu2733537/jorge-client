package com.jorge.modules.movement;

import com.jorge.core.*;

public class Phase extends HackModule {
    public Phase() { super("Phase", "Clip through blocks", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (mc.player.horizontalCollision && mc.player.isCrouching()) {
            double yaw = Math.toRadians(mc.player.getYRot());
            mc.player.setPos(mc.player.getX() - Math.sin(yaw) * 0.5, mc.player.getY(), mc.player.getZ() + Math.cos(yaw) * 0.5);
        }
    }
}
