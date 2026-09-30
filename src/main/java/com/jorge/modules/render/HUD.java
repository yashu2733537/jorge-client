package com.jorge.modules.render;

import com.jorge.core.*;

public class HUD extends HackModule {
    public final BooleanSetting watermark = add(new BooleanSetting("Watermark", true));
    public final BooleanSetting arraylist = add(new BooleanSetting("ArrayList", true));
    public final BooleanSetting coords = add(new BooleanSetting("Coords", true));
    public final BooleanSetting fps = add(new BooleanSetting("FPS", true));
    public HUD() {
        super("HUD", "Overlay HUD", Category.RENDER);
        setEnabled(true);
    }
}
