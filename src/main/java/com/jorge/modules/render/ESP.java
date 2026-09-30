package com.jorge.modules.render;

import com.jorge.core.*;

public class ESP extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "AABB", "AABB", "Glow", "Shader"));
    public final BooleanSetting players = add(new BooleanSetting("Players", true));
    public final BooleanSetting mobs = add(new BooleanSetting("Mobs", true));
    public final BooleanSetting chests = add(new BooleanSetting("Chests", false));
    public ESP() { super("ESP", "See entities through walls", Category.RENDER); }
    @Override public void onEnable() {
        if (!mode.is("Box") && mc.level != null) {
            for (var e : mc.level.entitiesForRendering()) e.setGlowingTag(true);
        }
    }
    @Override public void onDisable() {
        if (mc.level != null) for (var e : mc.level.entitiesForRendering()) e.setGlowingTag(false);
    }
    @Override public void onTick() {
        if (mode.is("Box") || mc.level == null) return;
        for (var e : mc.level.entitiesForRendering()) {
            if (e == mc.player) continue;
            e.setGlowingTag(true);
        }
    }
}
