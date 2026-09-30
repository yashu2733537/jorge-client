package com.jorge.modules.render;

import com.jorge.core.*;

public class Tracers extends HackModule {
    public final BooleanSetting players = add(new BooleanSetting("Players", true));
    public final BooleanSetting mobs = add(new BooleanSetting("Mobs", false));
    public Tracers() { super("Tracers", "Lines to entities", Category.RENDER); }
}
