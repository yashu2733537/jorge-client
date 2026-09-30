package com.jorge.core;

import java.util.function.Supplier;

public class NumberSetting extends Setting<Double> {
    private final double min, max, step;
    public NumberSetting(String name, double def, double min, double max, double step) {
        super(name, def);
        this.min = min; this.max = max; this.step = step;
    }
    public NumberSetting(String name, double def, double min, double max, double step, Supplier<Boolean> vis) {
        super(name, def, vis);
        this.min = min; this.max = max; this.step = step;
    }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }
    public float getFloat() { return value.floatValue(); }
    public int getInt() { return (int) Math.round(value); }
    @Override public void set(Double v) {
        if (v < min) v = min;
        if (v > max) v = max;
        // snap to step
        double steps = Math.round((v - min) / step);
        this.value = min + steps * step;
    }
}
