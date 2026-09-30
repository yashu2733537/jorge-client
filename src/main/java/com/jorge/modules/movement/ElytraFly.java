package com.jorge.modules.movement;

import com.jorge.core.*;

public class ElytraFly extends HackModule {
    public final NumberSetting speed = add(new NumberSetting("Speed", 1.0, 0.1, 3.0, 0.1));
    public ElytraFly() { super("ElytraFly", "Fly fast with elytra", Category.MOVEMENT); }
    @Override public String getSuffix() { return String.valueOf(speed.get()); }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isFallFlying()) return;
        float yaw = mc.player.getYRot();
        double s = 0.5 * speed.get();
        double rad = Math.toRadians(yaw);
        double vy = mc.player.getDeltaMovement().y;
        if (mc.options.keyJump.isDown()) vy = 0.5 * speed.get();
        else if (mc.options.keyShift.isDown()) vy = -0.5 * speed.get();
        else vy *= 0.98;
        mc.player.setDeltaMovement(-Math.sin(rad) * s, vy, Math.cos(rad) * s);
    }
}
