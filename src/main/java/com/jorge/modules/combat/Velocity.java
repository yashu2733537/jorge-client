package com.jorge.modules.combat;

import com.jorge.core.*;

public class Velocity extends HackModule {
    public final NumberSetting horizontal = add(new NumberSetting("Horizontal", 0.0, 0.0, 100.0, 1.0));
    public final NumberSetting vertical = add(new NumberSetting("Vertical", 0.0, 0.0, 100.0, 1.0));
    public final ModeSetting mode = add(new ModeSetting("Mode", "Cancel", "Cancel", "Reduce", "JumpReset"));
    public Velocity() { super("Velocity", "Reduces knockback", Category.COMBAT); }
    @Override public String getSuffix() { return mode.get(); }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (mode.is("JumpReset") && mc.player.hurtTime > 0 && mc.player.onGround() && mc.player.zza > 0) {
            mc.player.jumpFromGround();
        }
    }
    public static boolean shouldCancel() {
        return com.jorge.core.ModuleManager.INSTANCE.get(Velocity.class).isEnabled();
    }
}
