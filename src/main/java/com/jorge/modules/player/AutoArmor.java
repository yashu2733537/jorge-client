package com.jorge.modules.player;

import com.jorge.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public class AutoArmor extends HackModule {
    public final NumberSetting delay = add(new NumberSetting("Delay", 200.0, 50.0, 1000.0, 25.0));
    private long last = 0;
    public AutoArmor() { super("AutoArmor", "Equips best armor", Category.PLAYER); }
    @Override public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        if (System.currentTimeMillis() - last < delay.get()) return;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            int bestSlot = -1; int bestScore = score(mc.player.getItemBySlot(slot));
            for (int i = 9; i < 45; i++) {
                ItemStack s = mc.player.getInventory().getItem(i >= 36 ? i - 36 : i);
                if (slotOf(s) != slot) continue;
                int sc = score(s);
                if (sc > bestScore) { bestScore = sc; bestSlot = i; }
            }
            if (bestSlot != -1) {
                int syncSlot = bestSlot < 9 ? 36 + bestSlot : bestSlot;
                mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, syncSlot, 0, ContainerInput.QUICK_MOVE, mc.player);
                last = System.currentTimeMillis();
                return;
            }
        }
    }
    private static EquipmentSlot slotOf(ItemStack s) {
        String path = BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        if (path.endsWith("_helmet")) return EquipmentSlot.HEAD;
        if (path.endsWith("_chestplate")) return EquipmentSlot.CHEST;
        if (path.endsWith("_leggings")) return EquipmentSlot.LEGS;
        if (path.endsWith("_boots")) return EquipmentSlot.FEET;
        return null;
    }
    private static int score(ItemStack s) {
        String path = BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        boolean armor = path.endsWith("_helmet") || path.endsWith("_chestplate")
            || path.endsWith("_leggings") || path.endsWith("_boots");
        if (!armor) return -1;
        if (path.startsWith("netherite_")) return 7;
        if (path.startsWith("diamond_")) return 6;
        if (path.startsWith("iron_") || path.startsWith("turtle_")) return 5;
        if (path.startsWith("copper_")) return 4;
        if (path.startsWith("chainmail_")) return 3;
        if (path.startsWith("golden_")) return 2;
        if (path.startsWith("leather_")) return 1;
        return 0;
    }
}
