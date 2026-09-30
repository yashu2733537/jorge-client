package com.jorge.modules.render;

import com.jorge.core.*;

public class Nametags extends HackModule {
    public final NumberSetting scale = add(new NumberSetting("Scale", 1.0, 0.5, 2.0, 0.1));
    public final BooleanSetting health = add(new BooleanSetting("Health", true));
    public Nametags() { super("Nametags", "Bigger nametags", Category.RENDER); }
}
