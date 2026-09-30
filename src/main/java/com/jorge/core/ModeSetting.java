package com.jorge.core;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting<String> {
    private final List<String> modes;
    private int index;
    public ModeSetting(String name, String def, String... modes) {
        super(name, def);
        this.modes = Arrays.asList(modes);
        this.index = Math.max(0, this.modes.indexOf(def));
    }
    public ModeSetting(String name, String def, Supplier<Boolean> vis, String... modes) {
        super(name, def, vis);
        this.modes = Arrays.asList(modes);
        this.index = Math.max(0, this.modes.indexOf(def));
    }
    public List<String> getModes() { return modes; }
    public int getIndex() { return index; }
    public boolean is(String m) { return value.equalsIgnoreCase(m); }
    public void cycle() {
        index = (index + 1) % modes.size();
        value = modes.get(index);
    }
    @Override public void set(String v) {
        int i = modes.indexOf(v);
        if (i >= 0) { index = i; value = modes.get(i); }
    }
}
