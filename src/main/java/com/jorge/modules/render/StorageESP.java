package com.jorge.modules.render;

import com.jorge.core.*;

public class StorageESP extends HackModule {
    public final BooleanSetting chest = add(new BooleanSetting("Chest", true));
    public final BooleanSetting ender = add(new BooleanSetting("EnderChest", true));
    public final BooleanSetting shulker = add(new BooleanSetting("Shulker", true));
    public StorageESP() { super("StorageESP", "Highlights containers", Category.RENDER); }
}
