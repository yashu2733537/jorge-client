package com.jorge.modules.movement;

import com.jorge.core.*;

public class NoSlow extends HackModule {
    public static NoSlow INSTANCE;
    public final NumberSetting slow = add(new NumberSetting("Slow%", 100.0, 0.0, 100.0, 1.0));
    public NoSlow() { super("NoSlow", "No slowdown on use", Category.MOVEMENT); INSTANCE = this; }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (mc.player.isUsingItem() && slow.get() >= 99.9) {
            // compensate vanilla 0.2x slow by re-adding velocity along wish dir
            float yaw = mc.player.getYRot();
            double f = mc.player.zza, s = mc.player.xxa;
            if (f == 0 && s == 0) return;
            double rad = Math.toRadians(yaw);
            double boost = 0.22;
            double mx = (-Math.sin(rad) * f + Math.cos(rad) * s) * boost;
            double mz = (Math.cos(rad) * f + Math.sin(rad) * s) * boost;
            mc.player.push(mx * 0.35, 0, mz * 0.35);
        }
    }
}
