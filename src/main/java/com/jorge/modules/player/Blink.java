package com.jorge.modules.player;

import com.jorge.core.*;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import java.util.ArrayList;
import java.util.List;

public class Blink extends HackModule {
    private final List<ServerboundMovePlayerPacket> packets = new ArrayList<>();
    public Blink() { super("Blink", "Chokes packets / teleport", Category.PLAYER); }
    @Override public void onDisable() {
        if (mc.getConnection() != null) {
            for (var p : packets) mc.getConnection().send(p);
        }
        packets.clear();
    }
    public boolean choke(ServerboundMovePlayerPacket p) {
        if (isEnabled()) { packets.add(p); return true; }
        return false;
    }
}
