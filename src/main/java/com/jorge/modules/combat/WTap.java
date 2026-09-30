package com.jorge.modules.combat;

import com.jorge.core.*;
import net.minecraft.world.entity.player.Player;

public class WTap extends HackModule {
    public final NumberSetting delay = add(new NumberSetting("Delay", 300.0, 50.0, 1000.0, 25.0));
    private long lastTap = 0;
    public WTap() { super("WTap", "Sprint-taps for combos", Category.COMBAT); }
    @Override public void onTick() {
        if (mc.player == null || mc.hitResult == null) return;
        if (!(mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult ehr)) return;
        if (!(ehr.getEntity() instanceof Player)) return;
        if (mc.player.hurtTime > 0 || ((Player) ehr.getEntity()).hurtTime > 0) return;
        if (System.currentTimeMillis() - lastTap < delay.get()) return;
        if (mc.player.isSprinting()) {
            mc.player.setSprinting(false);
            mc.options.keyUp.setDown(false);
            lastTap = System.currentTimeMillis();
            new Thread(() -> {
                try { Thread.sleep(80); } catch (Exception ignored) {}
                if (mc.player != null) mc.player.setSprinting(true);
            }).start();
        }
    }
}
