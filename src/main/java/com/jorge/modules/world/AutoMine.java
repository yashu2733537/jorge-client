package com.jorge.modules.level;

import com.jorge.core.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Direction;

public class AutoMine extends HackModule {
    public AutoMine() { super("AutoMine", "Auto mines targeted block", Category.WORLD); }
    @Override public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        if (!(mc.hitResult instanceof net.minecraft.world.phys.BlockHitResult bhr)) return;
        if (!mc.options.keyAttack.isDown()) return;
        mc.gameMode.startDestroyBlock(bhr.getBlockPos(), Direction.UP);
        mc.player.swing(InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
    }
}
