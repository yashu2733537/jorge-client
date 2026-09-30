package com.jorge.modules.level;

import com.jorge.core.*;

public class FastPlace extends HackModule {
    public final NumberSetting delay = add(new NumberSetting("Delay", 0.0, 0.0, 4.0, 1.0));
    public FastPlace() { super("FastPlace", "Fast block place", Category.WORLD); }
    @Override public void onTick() {
        if (mc.player == null || mc.gameMode == null || mc.hitResult == null) return;
        if (!mc.options.keyUse.isDown()) return;
        if (mc.hitResult instanceof net.minecraft.world.phys.BlockHitResult bhr) {
            mc.gameMode.useItemOn(mc.player, net.minecraft.world.InteractionHand.MAIN_HAND, bhr);
            mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
        }
    }
}
