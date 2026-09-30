package com.jorge.core;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public abstract class HackModule {
    protected static final Minecraft mc = Minecraft.getInstance();
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;
    private int key = -1;
    protected final List<Setting<?>> settings = new ArrayList<>();

    public HackModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public List<Setting<?>> getSettings() { return settings; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

    protected <T extends Setting<?>> T add(T s) { settings.add(s); return s; }

    public void setEnabled(boolean on) {
        if (on == enabled) return;
        enabled = on;
        if (on) { onEnable(); } else { onDisable(); }
    }

    public void toggle() { setEnabled(!enabled); }

    protected void onEnable() {}
    protected void onDisable() {}

    // called every client tick when enabled
    public void onTick() {}
    // called every frame for world render / overlay hooks
    public void onRender() {}
    // suffix for arraylist, e.g. "6.0" for Reach
    public String getSuffix() { return ""; }
}
