package com.jorge.modules.player;

import com.jorge.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.inventory.ContainerInput;

public class InvManager extends HackModule {
    public final NumberSetting delay = add(new NumberSetting("Delay", 150.0, 50.0, 800.0, 25.0));
    public final BooleanSetting dropTrash = add(new BooleanSetting("DropTrash", true));
    private long last = 0;
    public InvManager() { super("InvManager", "Cleans inventory", Category.PLAYER); }
    @Override public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        if (mc.player.containerMenu == null) return;
        if (System.currentTimeMillis() - last < delay.get()) return;
        for (int i = 9; i < 45; i++) {
            var stack = mc.player.getInventory().getItem(i >= 36 ? i - 36 : i);
            if (stack.isEmpty()) continue;
            if (isTrash(stack)) {
                int sync = i < 36 ? i : i;
                // drop
                if (dropTrash.isOn()) {
                    mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, sync, 1, ContainerInput.THROW, mc.player);
                    last = System.currentTimeMillis();
                    return;
                }
            }
        }
    }
    private boolean isTrash(ItemStack s) {
        String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        if (path.endsWith("_sword")) return !(path.startsWith("diamond_") || path.startsWith("netherite_"));
        if (path.endsWith("_pickaxe") || path.endsWith("_axe") || path.endsWith("_shovel") || path.endsWith("_hoe")) return false;
        return s.getItem() == Items.STICK || s.getItem() == Items.STRING || s.getItem() == Items.ROTTEN_FLESH
            || s.getItem() == Items.SPIDER_EYE || s.getItem() == Items.POISONOUS_POTATO;
    }
}
