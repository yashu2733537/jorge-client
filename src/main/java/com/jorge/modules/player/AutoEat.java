package com.jorge.modules.player;

import com.jorge.core.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class AutoEat extends HackModule {
    public final NumberSetting hunger = add(new NumberSetting("Hunger", 14.0, 1.0, 20.0, 1.0));
    public AutoEat() { super("AutoEat", "Auto eats food", Category.PLAYER); }
    @Override public void onTick() {
        if (mc.player == null) return;
        if (mc.player.getFoodData().getFoodLevel() > hunger.get()) return;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.player.getInventory().getItem(i);
            boolean f = s.has(net.minecraft.core.component.DataComponents.FOOD);
            if (f) {
                mc.player.getInventory().setSelectedSlot(i);
                mc.options.keyUse.setDown(true);
                return;
            }
        }
        mc.options.keyUse.setDown(false);
    }
    @Override public void onDisable() { if (mc.options != null) mc.options.keyUse.setDown(false); }
}
