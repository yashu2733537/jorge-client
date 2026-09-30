package com.jorge.modules.combat;

import com.jorge.core.*;
import com.jorge.util.RotationUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class AutoBlock extends HackModule {
    public final NumberSetting range = add(new NumberSetting("Range", 4.0, 3.0, 6.0, 0.1));
    public AutoBlock() { super("AutoBlock", "Auto blocks with shield", Category.COMBAT); }
    @Override public void onTick() {
        if (mc.player == null || mc.level == null) return;
        Player target = null;
        double best = Double.MAX_VALUE;
        for (Player p : mc.level.players()) {
            if (p == mc.player || !p.isAlive() || p.isCreative()) continue;
            double d = mc.player.distanceTo(p);
            if (d <= range.get() && d < best) { best = d; target = p; }
        }
        boolean shield = mc.player.getMainHandItem().getItem() == Items.SHIELD
            || mc.player.getOffhandItem().getItem() == Items.SHIELD;
        if (target != null && shield) {
            RotationUtil.face(target);
            mc.options.keyUse.setDown(true);
        } else {
            mc.options.keyUse.setDown(false);
        }
    }
    @Override public void onDisable() {
        if (mc.options != null) mc.options.keyUse.setDown(false);
    }
}
