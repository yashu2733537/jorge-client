package com.jorge.modules.combat;

import com.jorge.core.*;

public class AutoClicker extends HackModule {
    public final NumberSetting minCps = add(new NumberSetting("MinCPS", 9.0, 1.0, 20.0, 0.5));
    public final NumberSetting maxCps = add(new NumberSetting("MaxCPS", 13.0, 1.0, 20.0, 0.5));
    public final BooleanSetting leftOnly = add(new BooleanSetting("LeftOnly", true));
    public final BooleanSetting breakBlocks = add(new BooleanSetting("BreakBlocks", true));
    private long nextClick = 0;

    public AutoClicker() { super("AutoClicker", "Clicks automatically", Category.COMBAT); }

    @Override public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        boolean attacking = mc.options.keyAttack.isDown();
        boolean using = mc.options.keyUse.isDown();
        boolean should = attacking || (!leftOnly.isOn() && using);
        if (!should) return;
        long now = System.currentTimeMillis();
        if (now < nextClick) return;
        double cps = minCps.get() + Math.random() * Math.max(0.1, maxCps.get() - minCps.get());
        nextClick = now + (long) (1000.0 / cps) + (long) (Math.random() * 15);
        if (attacking) {
            if (mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult ehr) {
                com.jorge.modules.combat.Criticals.doCrit();
                mc.gameMode.attack(mc.player, ehr.getEntity());
                mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
            } else if (breakBlocks.isOn() && mc.hitResult instanceof net.minecraft.world.phys.BlockHitResult bhr) {
                mc.gameMode.startDestroyBlock(bhr.getBlockPos(), bhr.getDirection());
                mc.player.swing(net.minecraft.world.InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
            }
        } else if (using) {
            mc.gameMode.useItem(mc.player, net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }
}
