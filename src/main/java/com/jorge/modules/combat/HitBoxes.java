package com.jorge.modules.combat;

import com.jorge.core.*;

public class HitBoxes extends HackModule {
    public static HitBoxes INSTANCE;
    public final NumberSetting expand = add(new NumberSetting("Expand", 0.3, 0.0, 1.0, 0.05));
    public HitBoxes() { super("HitBoxes", "Bigger entity hitboxes", Category.COMBAT); INSTANCE = this; }
}
