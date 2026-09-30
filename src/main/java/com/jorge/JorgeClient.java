package com.jorge;

import com.jorge.core.HackModule;
import com.jorge.core.ModuleManager;
import com.jorge.ui.ClickGuiScreen;
import com.jorge.ui.HudOverlay;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class JorgeClient implements ClientModInitializer {
    public static final String MODID = "jorge-client";
    private boolean rshiftDown = false;

    @Override
    public void onInitializeClient() {
        ModuleManager.INSTANCE.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ModuleManager.INSTANCE.onTick();
            // Right Shift toggles the ClickGUI (edge-triggered, no KeyMapping needed)
            boolean down = InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
            if (down && !rshiftDown) {
                if (client.gui.screen() instanceof ClickGuiScreen) client.gui.setScreen(null);
                else client.gui.setScreen(new ClickGuiScreen());
            }
            rshiftDown = down;
            for (HackModule m : ModuleManager.INSTANCE.getAll()) {
                if (m.getKey() != -1 && InputConstants.isKeyDown(m.getKey())) {
                    if (!KeyDebounce.isDown(m.getKey())) {
                        m.toggle();
                        KeyDebounce.setDown(m.getKey(), true);
                    }
                } else if (m.getKey() != -1) {
                    KeyDebounce.setDown(m.getKey(), false);
                }
            }
        });

        HudOverlay.register();
    }

    private static class KeyDebounce {
        private static final java.util.Set<Integer> down = new java.util.HashSet<>();
        static boolean isDown(int k) { return down.contains(k); }
        static void setDown(int k, boolean v) { if (v) down.add(k); else down.remove(k); }
    }
}
