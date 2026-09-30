package com.jorge.modules.render;

import com.jorge.core.*;

public class Chams extends HackModule {
    public final BooleanSetting players = add(new BooleanSetting("Players", true));
    public Chams() { super("Chams", "See players through walls", Category.RENDER); }
}
