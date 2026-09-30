package com.jorge.modules.render;

import com.jorge.core.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullBright extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Gamma", "Gamma", "Potion"));
    public FullBright() { super("FullBright", "Full brightness", Category.RENDER); }
    @Override public void onDisable() {
        if (mc.player == null) return;
        mc.player.removeEffect(MobEffects.NIGHT_VISION);
    }
    @Override public void onTick() {
        // 26.3 has no gamma option to max out; Night Vision gives full brightness in both modes
        if (mc.player != null && !mc.player.hasEffect(MobEffects.NIGHT_VISION)) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000000, 0, false, false));
        }
    }
}
