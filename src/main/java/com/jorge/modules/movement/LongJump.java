package com.jorge.modules.movement;

import com.jorge.core.*;

public class LongJump extends HackModule {
    public final NumberSetting boost = add(new NumberSetting("Boost", 1.5, 0.5, 4.0, 0.1));
    private int ticks = 0;
    public LongJump() { super("LongJump", "Jump far", Category.MOVEMENT); }
    @Override public void onEnable() {
        if (mc.player == null) return;
        if (mc.player.onGround()) {
            mc.player.jumpFromGround();
            ticks = 0;
        }
    }
    @Override public void onTick() {
        if (mc.player == null) return;
        ticks++;
        if (ticks < 10 && mc.player.zza > 0) {
            float yaw = mc.player.getYRot();
            double rad = Math.toRadians(yaw);
            double s = 0.35 * boost.get();
            mc.player.setDeltaMovement(-Math.sin(rad) * s, mc.player.getDeltaMovement().y, Math.cos(rad) * s);
        } else if (ticks >= 10) setEnabled(false);
    }
}
