package com.jorge.modules.movement;

import com.jorge.core.*;

public class Fly extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Creative", "Creative", "Vanilla", "Jetpack"));
    public final NumberSetting speed = add(new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1));
    public Fly() { super("Fly", "Fly like creative", Category.MOVEMENT); }
    @Override public String getSuffix() { return mode.get(); }
    @Override public void onEnable() {
        if (mode.is("Creative") && mc.player != null) mc.player.getAbilities().flying = true;
    }
    @Override public void onDisable() {
        if (mc.player != null && !mc.player.isCreative()) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().flying = false;
        }
    }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (mode.is("Creative")) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlyingSpeed(speed.getFloat() * 0.05f);
        } else if (mode.is("Vanilla")) {
            mc.player.getAbilities().flying = false;
            mc.player.fallDistance = 0;
            double s = 0.6 * speed.get();
            if (mc.player.isSprinting()) s *= 1.4;
            float yaw = mc.player.getYRot();
            double f = mc.player.zza, st = mc.player.xxa;
            double rad = Math.toRadians(yaw);
            double mx = (-Math.sin(rad) * f + Math.cos(rad) * st) * s;
            double mz = (Math.cos(rad) * f + Math.sin(rad) * st) * s;
            double my = 0;
            if (mc.options.keyJump.isDown()) my += 0.6 * speed.get();
            if (mc.options.keyShift.isDown()) my -= 0.6 * speed.get();
            mc.player.setDeltaMovement(mx, my, mz);
        } else if (mode.is("Jetpack")) {
            mc.player.fallDistance = 0;
            var d = mc.player.getDeltaMovement();
            double mx = d.x, mz = d.z;
            double my = d.y;
            if (mc.options.keyJump.isDown()) my = Math.min(my + 0.15 * speed.get(), 0.8 * speed.get());
            double[] dir = direction(0.25 * speed.get());
            mc.player.setDeltaMovement((mx + dir[0]) * 0.98, my, (mz + dir[1]) * 0.98);
        }
    }
    private double[] direction(double s) {
        float yaw = mc.player.getYRot();
        double f = mc.player.zza, st = mc.player.xxa;
        double rad = Math.toRadians(yaw);
        double mx = (-Math.sin(rad) * f + Math.cos(rad) * st) * s;
        double mz = (Math.cos(rad) * f + Math.sin(rad) * st) * s;
        return new double[]{mx, mz};
    }
}
