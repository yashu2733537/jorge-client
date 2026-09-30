package com.jorge.core;

import com.jorge.modules.combat.*;
import com.jorge.modules.movement.*;
import com.jorge.modules.player.*;
import com.jorge.modules.render.*;
import com.jorge.modules.level.*;
import com.jorge.modules.exploit.*;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    public static final ModuleManager INSTANCE = new ModuleManager();
    private final List<HackModule> modules = new ArrayList<>();

    private ModuleManager() {}

    public void init() {
        // COMBAT
        register(new KillAura(), new AutoClicker(), new Reach(), new Velocity(),
            new Criticals(), new AutoBlock(), new HitBoxes(), new WTap(), new AntiBot(), new AutoTotem());
        // MOVEMENT
        register(new Sprint(), new Speed(), new Fly(), new LongJump(), new Step(),
            new NoSlow(), new Scaffold(), new Spider(), new Jesus(), new NoFall(),
            new Phase(), new SafeWalk(), new ElytraFly());
        // PLAYER
        register(new AutoEat(), new AutoArmor(), new ChestStealer(), new InvManager(),
            new AutoTool(), new FastBreak(), new Blink(), new NoRotate(), new AutoRespawn());
        // RENDER
        register(new ESP(), new Tracers(), new Nametags(), new FullBright(),
            new HUD(), new StorageESP(), new ItemESP(), new Chams(), new XRay());
        // WORLD
        register(new Nuker(), new AutoMine(), new FastPlace(), new TimerMod());
        // EXPLOIT
        register(new Disabler(), new PacketFly());
    }

    private void register(HackModule... mods) { modules.addAll(Arrays.asList(mods)); }

    public List<HackModule> getAll() { return modules; }
    public List<HackModule> byCategory(Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }
    public <T extends HackModule> T get(Class<T> cls) {
        for (HackModule m : modules) if (cls.isInstance(m)) return cls.cast(m);
        return null;
    }
    public HackModule byName(String n) {
        for (HackModule m : modules) if (m.getName().equalsIgnoreCase(n)) return m;
        return null;
    }
    public void onTick() {
        for (HackModule m : modules) if (m.isEnabled()) {
            try { m.onTick(); } catch (Exception ignored) {}
        }
    }
}
