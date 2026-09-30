package com.jorge.modules.player;

import com.jorge.core.*;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ContainerInput;

public class ChestStealer extends HackModule {
    public final NumberSetting delay = add(new NumberSetting("Delay", 100.0, 0.0, 500.0, 10.0));
    private long last = 0;
    public ChestStealer() { super("ChestStealer", "Steals chest loot", Category.PLAYER); }
    @Override public void onTick() {
        if (!(mc.gui.screen() instanceof ContainerScreen)) return;
        if (System.currentTimeMillis() - last < delay.get()) return;
        var handler = mc.player.containerMenu;
        int size = handler.slots.size() - 36;
        for (int i = 0; i < size; i++) {
            var stack = handler.getSlot(i).getItem();
            if (!stack.isEmpty()) {
                mc.gameMode.handleContainerInput(handler.containerId, i, 0, ContainerInput.QUICK_MOVE, mc.player);
                last = System.currentTimeMillis();
                return;
            }
        }
        mc.player.closeContainer();
    }
}
