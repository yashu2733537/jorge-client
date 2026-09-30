package com.jorge.modules.combat;

import com.jorge.core.*;

public class Reach extends HackModule {
    public static Reach INSTANCE;
    public final NumberSetting reach = add(new NumberSetting("Reach", 4.5, 3.0, 6.0, 0.1));
    public Reach() { super("Reach", "Extended attack distance", Category.COMBAT); INSTANCE = this; }
    @Override public String getSuffix() { return String.valueOf(reach.get()); }
    public static double getReach() {
        if (INSTANCE != null && INSTANCE.isEnabled()) return INSTANCE.reach.get();
        return 3.0;
    }
}
