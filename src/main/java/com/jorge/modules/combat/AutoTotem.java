package com.jorge.modules.combat;

import com.jorge.core.*;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;

public class AutoTotem extends HackModule {
    public final NumberSetting health = add(new NumberSetting("Health", 10.0, 1.0, 20.0, 0.5));
    public final BooleanSetting always = add(new BooleanSetting("Always", true));
    public AutoTotem() { super("AutoTotem", "Auto offhand totem", Category.COMBAT); }
    @Override public void onTick() {
        if (mc.player == null || mc.gameMode == null || mc.player.containerMenu == null) return;
        if (mc.player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) return;
        if (!always.isOn() && mc.player.getHealth() > health.get()) return;
        if (!mc.player.getOffhandItem().isEmpty()) return;
        for (int i = 9; i < 45; i++) {
            var s = mc.player.getInventory().getItem(i >= 36 ? i - 36 : i);
            if (s.getItem() == Items.TOTEM_OF_UNDYING) {
                mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, i, 0, ContainerInput.QUICK_MOVE, mc.player);
                return;
            }
        }
    }
}
