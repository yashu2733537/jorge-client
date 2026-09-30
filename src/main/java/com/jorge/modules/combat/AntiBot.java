package com.jorge.modules.combat;

import com.jorge.core.*;
import net.minecraft.world.entity.player.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AntiBot extends HackModule {
    private final Set<UUID> realPlayers = new HashSet<>();
    public AntiBot() { super("AntiBot", "Ignores fake bots", Category.COMBAT); }
    @Override public void onTick() {
        if (mc.level == null || mc.getConnection() == null) return;
        realPlayers.clear();
        mc.getConnection().getOnlinePlayers().forEach(e -> realPlayers.add(e.getProfile().id()));
        // KillAura checks via isBot()
    }
    public boolean isBot(Player p) {
        if (!isEnabled()) return false;
        return !realPlayers.contains(p.getUUID());
    }
}
