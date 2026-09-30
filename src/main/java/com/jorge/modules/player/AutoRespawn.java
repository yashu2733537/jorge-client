package com.jorge.modules.player;

import com.jorge.core.*;

public class AutoRespawn extends HackModule {
    public AutoRespawn() { super("AutoRespawn", "Auto respawn on death", Category.PLAYER); }
    @Override public void onTick() {
        if (mc.player != null && mc.player.isDeadOrDying() && mc.gui.screen() instanceof net.minecraft.client.gui.screens.DeathScreen) {
            mc.getConnection().send(new net.minecraft.network.protocol.game.ServerboundClientCommandPacket(net.minecraft.network.protocol.game.ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            mc.gui.setScreen(null);
        }
    }
}
