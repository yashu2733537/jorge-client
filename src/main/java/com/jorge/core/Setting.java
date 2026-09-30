package com.jorge.core;

import java.util.function.Supplier;

public abstract class Setting<T> {
    protected final String name;
    protected T value;
    protected final Supplier<Boolean> visible;

    public Setting(String name, T value, Supplier<Boolean> visible) {
        this.name = name;
        this.value = value;
        this.visible = visible == null ? () -> true : visible;
    }
    public Setting(String name, T value) { this(name, value, null); }

    public String getName() { return name; }
    public T get() { return value; }
    public void set(T v) { this.value = v; }
    public boolean isVisible() { return visible.get(); }
}
