package com.jorge.modules.movement;

import com.jorge.core.*;

public class Speed extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Strafe", "Strafe", "Hop", "LowHop", "MatrixHop"));
    public final NumberSetting speed = add(new NumberSetting("Speed", 1.2, 0.5, 3.0, 0.05));
    public Speed() { super("Speed", "Move faster", Category.MOVEMENT); }
    @Override public String getSuffix() { return mode.get(); }
    @Override public void onTick() {
        if (mc.player == null || !mc.player.onGround() && mode.is("Hop")) return;
        if (mc.player == null) return;
        float yaw = mc.player.getYRot();
        double forward = mc.player.zza, strafe = mc.player.xxa;
        if (forward == 0 && strafe == 0) return;
        double rad = Math.toRadians(yaw);
        double sin = Math.sin(rad), cos = Math.cos(rad);
        double mx = 0, mz = 0;
        double base = 0.2873 * speed.get();
        if (mode.is("Strafe")) {
            if (mc.player.onGround()) mc.player.jumpFromGround();
            mx = (-sin * forward + cos * strafe) * base;
            mz = (cos * forward + sin * strafe) * base;
            mc.player.setDeltaMovement(mx, mc.player.getDeltaMovement().y, mz);
        } else if (mode.is("Hop") || mode.is("MatrixHop")) {
            if (mc.player.onGround()) {
                mc.player.jumpFromGround();
                mx = (-sin * forward + cos * strafe) * base;
                mz = (cos * forward + sin * strafe) * base;
                mc.player.setDeltaMovement(mx, 0.42, mz);
            }
        } else if (mode.is("LowHop")) {
            if (mc.player.onGround()) {
                mc.player.jumpFromGround();
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x * 1.02, 0.3, mc.player.getDeltaMovement().z * 1.02);
            }
        }
    }
}
