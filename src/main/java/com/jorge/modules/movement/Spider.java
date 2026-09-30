package com.jorge.modules.movement;

import com.jorge.core.*;

public class Spider extends HackModule {
    public final NumberSetting speed = add(new NumberSetting("Speed", 0.3, 0.1, 1.0, 0.05));
    public Spider() { super("Spider", "Climb walls", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (mc.player.horizontalCollision && mc.player.zza > 0) {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, speed.get(), mc.player.getDeltaMovement().z);
        }
    }
}
