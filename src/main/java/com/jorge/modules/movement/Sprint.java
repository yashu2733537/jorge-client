package com.jorge.modules.movement;

import com.jorge.core.*;

public class Sprint extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Legit", "Legit", "Omni"));
    public Sprint() { super("Sprint", "Auto sprint", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (mode.is("Legit")) {
            if (mc.player.zza > 0 && !mc.player.isCrouching() && mc.player.getFoodData().getFoodLevel() > 6)
                mc.player.setSprinting(true);
        } else {
            if (mc.player.zza > 0 || mc.player.xxa != 0)
                mc.player.setSprinting(true);
        }
    }
}
