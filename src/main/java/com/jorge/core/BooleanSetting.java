package com.jorge.core;

import java.util.function.Supplier;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean def) { super(name, def); }
    public BooleanSetting(String name, boolean def, Supplier<Boolean> vis) { super(name, def, vis); }
    public boolean isOn() { return value; }
    public void toggle() { value = !value; }
}
