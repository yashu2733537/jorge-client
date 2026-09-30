package com.jorge.modules.movement;

import com.jorge.core.*;

public class Step extends HackModule {
    public final NumberSetting height = add(new NumberSetting("Height", 2.0, 1.0, 5.0, 0.5));
    public Step() { super("Step", "Step up blocks", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null) return;
        var attr = mc.player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.STEP_HEIGHT);
        if (attr != null) attr.setBaseValue(height.get());
    }
    @Override public void onDisable() {
        if (mc.player != null) {
            var attr = mc.player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.STEP_HEIGHT);
            if (attr != null) attr.setBaseValue(0.6);
        }
    }
}
