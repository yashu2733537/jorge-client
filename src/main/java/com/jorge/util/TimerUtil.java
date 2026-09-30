package com.jorge.util;

public class TimerUtil {
    private long last = System.currentTimeMillis();
    public boolean hasElapsed(long ms) { return System.currentTimeMillis() - last >= ms; }
    public void reset() { last = System.currentTimeMillis(); }
}
