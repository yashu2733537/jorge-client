package com.jorge.modules.combat;

import com.jorge.core.*;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Criticals extends HackModule {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Packet", "Packet", "Jump", "MiniJump"));
    public Criticals() { super("Criticals", "Always crits", Category.COMBAT); }
    // call before attack from KillAura or via tick when falling
    public static void doCrit() {
        try {
            var mm = ModuleManager.INSTANCE.get(Criticals.class);
            if (mm == null || !mm.isEnabled() || mc.player == null || mc.getConnection() == null) return;
            if (mc.player.onGround()) {
                double x = mc.player.getX(), y = mc.player.getY(), z = mc.player.getZ();
                if (mm.mode.is("Packet")) {
                    mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(x, y + 0.0625, z, false, false));
                    mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(x, y, z, false, false));
                } else if (mm.mode.is("Jump")) {
                    mc.player.jumpFromGround();
                } else if (mm.mode.is("MiniJump")) {
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.2, mc.player.getDeltaMovement().z);
                }
            }
        } catch (Exception ignored) {}
    }
}
