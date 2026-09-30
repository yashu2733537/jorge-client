package com.jorge.core;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    WORLD("World"),
    EXPLOIT("Exploit");

    public final String display;
    Category(String display) { this.display = display; }
}
