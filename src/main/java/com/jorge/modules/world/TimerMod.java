package com.jorge.modules.level;

import com.jorge.core.*;

public class TimerMod extends HackModule {
    public final NumberSetting speed = add(new NumberSetting("Speed", 1.5, 0.1, 4.0, 0.1));
    public TimerMod() { super("Timer", "Game speed", Category.WORLD); }
    @Override public String getSuffix() { return String.valueOf(speed.get()); }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (speed.get() > 1.0) {
            // soft-timer: extra movement ticks scaled by speed
            float yaw = mc.player.getYRot();
            double f = mc.player.zza, s = mc.player.xxa;
            if (f == 0 && s == 0) return;
            double rad = Math.toRadians(yaw);
            double extra = 0.06 * (speed.get() - 1.0);
            mc.player.push(-Math.sin(rad) * f * extra + Math.cos(rad) * s * extra, 0,
                Math.cos(rad) * f * extra + Math.sin(rad) * s * extra);
        }
    }
}
