package com.jorge.modules.movement;

import com.jorge.core.*;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFall extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Packet", "Packet", "Spoof", "NoGround"));
    public NoFall() { super("NoFall", "No fall damage", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null || mc.getConnection() == null) return;
        if (mc.player.fallDistance > 3) {
            if (mode.is("Packet")) {
                mc.getConnection().send(new ServerboundMovePlayerPacket.StatusOnly(true, false));
                mc.player.fallDistance = 0;
            } else if (mode.is("Spoof")) {
                mc.player.fallDistance = 0;
            }
        }
    }
}
