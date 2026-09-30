package com.jorge.modules.render;

import com.jorge.core.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XRay extends HackModule {
    public record Spot(BlockPos pos, int color, String key) {}
    public static final List<Spot> SPOTS = new CopyOnWriteArrayList<>();

    public final NumberSetting radius = add(new NumberSetting("Radius", 24.0, 10.0, 40.0, 2.0));
    public final BooleanSetting diamond = add(new BooleanSetting("Diamond", true));
    public final BooleanSetting gold = add(new BooleanSetting("Gold", true));
    public final BooleanSetting iron = add(new BooleanSetting("Iron", true));
    public final BooleanSetting redstone = add(new BooleanSetting("Redstone", true));
    public final BooleanSetting lapis = add(new BooleanSetting("Lapis", true));
    public final BooleanSetting emerald = add(new BooleanSetting("Emerald", true));
    public final BooleanSetting coal = add(new BooleanSetting("Coal", false));
    public final BooleanSetting quartz = add(new BooleanSetting("Quartz", true));
    public final BooleanSetting debris = add(new BooleanSetting("Debris", true));

    public XRay() { super("XRay", "Highlights ores through walls", Category.RENDER); }

    public boolean show(String key) {
        return switch (key) {
            case "diamond" -> diamond.isOn();
            case "gold" -> gold.isOn();
            case "iron" -> iron.isOn();
            case "redstone" -> redstone.isOn();
            case "lapis" -> lapis.isOn();
            case "emerald" -> emerald.isOn();
            case "coal" -> coal.isOn();
            case "quartz" -> quartz.isOn();
            case "debris" -> debris.isOn();
            default -> true;
        };
    }

    @Override public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (mc.player.tickCount % 10 != 0) return;
        SPOTS.clear();
        BlockPos origin = mc.player.blockPosition();
        int r = radius.getInt();
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-r, -r, -r), origin.offset(r, r, r))) {
            if (SPOTS.size() >= 200) break;
            var state = mc.level.getBlockState(pos);
            if (state.isAir()) continue;
            String key = keyOf(state.getBlock());
            if (key == null) continue;
            SPOTS.add(new Spot(pos.immutable(), colorOf(key), key));
        }
    }

    @Override public void onDisable() { SPOTS.clear(); }

    private static String keyOf(Block b) {
        if (b == Blocks.DIAMOND_ORE || b == Blocks.DEEPSLATE_DIAMOND_ORE) return "diamond";
        if (b == Blocks.GOLD_ORE || b == Blocks.DEEPSLATE_GOLD_ORE || b == Blocks.NETHER_GOLD_ORE) return "gold";
        if (b == Blocks.IRON_ORE || b == Blocks.DEEPSLATE_IRON_ORE) return "iron";
        if (b == Blocks.REDSTONE_ORE || b == Blocks.DEEPSLATE_REDSTONE_ORE) return "redstone";
        if (b == Blocks.LAPIS_ORE || b == Blocks.DEEPSLATE_LAPIS_ORE) return "lapis";
        if (b == Blocks.EMERALD_ORE || b == Blocks.DEEPSLATE_EMERALD_ORE) return "emerald";
        if (b == Blocks.COAL_ORE || b == Blocks.DEEPSLATE_COAL_ORE) return "coal";
        if (b == Blocks.NETHER_QUARTZ_ORE) return "quartz";
        if (b == Blocks.ANCIENT_DEBRIS) return "debris";
        return null;
    }

    private static int colorOf(String key) {
        return switch (key) {
            case "diamond" -> 0xFF00FFFF;
            case "gold" -> 0xFFFFD700;
            case "iron" -> 0xFFD8BFA8;
            case "redstone" -> 0xFFFF0000;
            case "lapis" -> 0xFF3050FF;
            case "emerald" -> 0xFF00FF00;
            case "coal" -> 0xFF808080;
            case "quartz" -> 0xFFFFFFFF;
            case "debris" -> 0xFF8B4513;
            default -> 0xFFFFFFFF;
        };
    }
}
