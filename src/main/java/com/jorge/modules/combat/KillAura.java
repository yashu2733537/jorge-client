package com.jorge.modules.combat;

import com.jorge.core.*;
import com.jorge.util.RotationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

public class KillAura extends HackModule {
    public final NumberSetting range = add(new NumberSetting("Range", 4.2, 3.0, 6.0, 0.1));
    public final NumberSetting aps = add(new NumberSetting("APS", 12.0, 1.0, 20.0, 0.5));
    public final ModeSetting priority = add(new ModeSetting("Priority", "Distance", "Distance", "Health", "Angle"));
    public final ModeSetting rotation = add(new ModeSetting("Rotation", "Snap", "Snap", "Silent", "None"));
    public final BooleanSetting players = add(new BooleanSetting("Players", true));
    public final BooleanSetting mobs = add(new BooleanSetting("Mobs", true));
    public final BooleanSetting animals = add(new BooleanSetting("Animals", false));
    public final BooleanSetting autosprint = add(new BooleanSetting("AutoSprint", true));
    public final BooleanSetting weaponsOnly = add(new BooleanSetting("WeaponsOnly", true));
    public final NumberSetting minCharge = add(new NumberSetting("MinCharge", 0.9, 0.0, 1.0, 0.05));
    public final ModeSetting weaponPref = add(new ModeSetting("Weapon", "Best", "Best", "Sword", "Axe"));
    public final BooleanSetting setZombie = add(new BooleanSetting("Zombies", true, () -> mobs.isOn()));
    public final BooleanSetting setSkeleton = add(new BooleanSetting("Skeletons", true, () -> mobs.isOn()));
    public final BooleanSetting setSpider = add(new BooleanSetting("Spiders", true, () -> mobs.isOn()));
    public final BooleanSetting setCreeper = add(new BooleanSetting("Creepers", true, () -> mobs.isOn()));
    public final BooleanSetting setEnderman = add(new BooleanSetting("Endermen", true, () -> mobs.isOn()));
    public final BooleanSetting setWitch = add(new BooleanSetting("Witches", true, () -> mobs.isOn()));
    public final BooleanSetting setBlaze = add(new BooleanSetting("Blazes", true, () -> mobs.isOn()));
    public final BooleanSetting setGhast = add(new BooleanSetting("Ghasts", true, () -> mobs.isOn()));
    public final BooleanSetting setSlime = add(new BooleanSetting("Slimes", true, () -> mobs.isOn()));
    public final BooleanSetting setPhantom = add(new BooleanSetting("Phantoms", true, () -> mobs.isOn()));
    public final BooleanSetting setPiglin = add(new BooleanSetting("Piglins", true, () -> mobs.isOn()));
    public final BooleanSetting setDrowned = add(new BooleanSetting("Drowned", true, () -> mobs.isOn()));
    public final BooleanSetting setGolem = add(new BooleanSetting("IronGolems", true, () -> mobs.isOn()));
    public final BooleanSetting setIllager = add(new BooleanSetting("Illagers", true, () -> mobs.isOn()));
    private long lastAttack = 0;

    public KillAura() { super("KillAura", "Auto attacks entities around you", Category.COMBAT); }

    @Override public String getSuffix() { return range.get() + " | " + aps.getInt(); }

    @Override public void onTick() {
        if (mc.player == null || mc.level == null) return;
        autoEquip();
        Entity target = null;
        double best = Double.MAX_VALUE;
        double lowestHp = Double.MAX_VALUE;
        for (Entity e : mc.level.entitiesForRendering()) {
            if (!RotationUtil.isValidTarget(e, range.get(), players.isOn(), mobs.isOn(), animals.isOn(), false)) continue;
            if (!isMobAllowed(e)) continue;
            if (e instanceof Player pe && pe.isDeadOrDying()) continue;
            double d = mc.player.distanceTo(e);
            if (priority.is("Distance") && d < best) { best = d; target = e; }
            else if (priority.is("Health") && e instanceof net.minecraft.world.entity.LivingEntity le && le.getHealth() < lowestHp) { lowestHp = le.getHealth(); target = e; }
            else if (priority.is("Angle")) {
                float[] r = RotationUtil.getRotations(e);
                float diff = Math.abs(r[0] - mc.player.getYRot());
                if (diff < best) { best = diff; target = e; }
            }
        }
        if (target == null) return;
        if (!rotation.is("None")) RotationUtil.face(target);
        long delay = (long) (1000.0 / Math.max(1, aps.get()));
        if (System.currentTimeMillis() - lastAttack < delay) return;
        if (weaponsOnly.isOn() && !isWeapon(mc.player.getMainHandItem())) return;
        if (mc.player.getAttackStrengthScale(0.5f) < minCharge.get()) return;
        if (autosprint.isOn()) mc.player.setSprinting(true);
        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
        lastAttack = System.currentTimeMillis();
    }
    private static boolean isWeapon(net.minecraft.world.item.ItemStack s) {
        String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        return path.endsWith("_sword") || path.endsWith("_axe") || path.equals("mace") || path.equals("trident");
    }
    private void autoEquip() {
        if (mc.player == null) return;
        int cur = mc.player.getInventory().getSelectedSlot();
        int best = -1, bestScore = weaponScore(mc.player.getInventory().getItem(cur));
        for (int i = 0; i < 9; i++) {
            int sc = weaponScore(mc.player.getInventory().getItem(i));
            if (sc > bestScore) { bestScore = sc; best = i; }
        }
        if (best != -1) mc.player.getInventory().setSelectedSlot(best);
    }
    private int weaponScore(ItemStack s) {
        String path = BuiltInRegistries.ITEM.getKey(s.getItem()).getPath();
        boolean sword = path.endsWith("_sword"), axe = path.endsWith("_axe");
        String pref = weaponPref.get();
        if (pref.equals("Sword") && !sword) return -1;
        if (pref.equals("Axe") && !axe) return -1;
        if (!sword && !axe) {
            if (path.equals("mace") || path.equals("trident")) return 4;
            return -1;
        }
        if (path.startsWith("netherite_")) return 6;
        if (path.startsWith("diamond_")) return 5;
        if (path.startsWith("iron_")) return 4;
        if (path.startsWith("copper_") || path.startsWith("stone_")) return 3;
        return 2;
    }
    private boolean isMobAllowed(Entity e) {
        if (e instanceof net.minecraft.world.entity.player.Player) return players.isOn();
        String key = mobKey(e);
        if (key != null) {
            if (!mobs.isOn()) return false;
            return mobSetting(key);
        }
        return animals.isOn();
    }
    private static String mobKey(Entity e) {
        var t = e.getType();
        if (t == EntityTypes.ZOMBIE || t == EntityTypes.HUSK) return "zombie";
        if (t == EntityTypes.SKELETON || t == EntityTypes.STRAY || t == EntityTypes.WITHER_SKELETON) return "skeleton";
        if (t == EntityTypes.SPIDER || t == EntityTypes.CAVE_SPIDER) return "spider";
        if (t == EntityTypes.CREEPER) return "creeper";
        if (t == EntityTypes.ENDERMAN) return "enderman";
        if (t == EntityTypes.WITCH) return "witch";
        if (t == EntityTypes.BLAZE) return "blaze";
        if (t == EntityTypes.GHAST) return "ghast";
        if (t == EntityTypes.SLIME || t == EntityTypes.MAGMA_CUBE) return "slime";
        if (t == EntityTypes.PHANTOM) return "phantom";
        if (t == EntityTypes.PIGLIN || t == EntityTypes.PIGLIN_BRUTE || t == EntityTypes.HOGLIN || t == EntityTypes.ZOMBIFIED_PIGLIN) return "piglin";
        if (t == EntityTypes.DROWNED) return "drowned";
        if (t == EntityTypes.IRON_GOLEM) return "golem";
        if (t == EntityTypes.PILLAGER || t == EntityTypes.VINDICATOR || t == EntityTypes.EVOKER) return "illager";
        return null;
    }
    private boolean mobSetting(String key) {
        return switch (key) {
            case "zombie" -> setZombie.isOn();
            case "skeleton" -> setSkeleton.isOn();
            case "spider" -> setSpider.isOn();
            case "creeper" -> setCreeper.isOn();
            case "enderman" -> setEnderman.isOn();
            case "witch" -> setWitch.isOn();
            case "blaze" -> setBlaze.isOn();
            case "ghast" -> setGhast.isOn();
            case "slime" -> setSlime.isOn();
            case "phantom" -> setPhantom.isOn();
            case "piglin" -> setPiglin.isOn();
            case "drowned" -> setDrowned.isOn();
            case "golem" -> setGolem.isOn();
            case "illager" -> setIllager.isOn();
            default -> true;
        };
    }
}
