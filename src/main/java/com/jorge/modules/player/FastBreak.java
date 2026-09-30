package com.jorge.modules.player;

import com.jorge.core.*;

public class FastBreak extends HackModule {
    public final NumberSetting mult = add(new NumberSetting("Multiplier", 1.5, 1.0, 3.0, 0.1));
    public FastBreak() { super("FastBreak", "Mine faster", Category.PLAYER); }
    public static float getMult() {
        var m = ModuleManager.INSTANCE.get(FastBreak.class);
        return (m != null && m.isEnabled()) ? m.mult.getFloat() : 1f;
    }
}
