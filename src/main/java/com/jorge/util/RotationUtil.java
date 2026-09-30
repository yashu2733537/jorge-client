package com.jorge.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RotationUtil {
    static final Minecraft mc = Minecraft.getInstance();

    public static float[] getRotations(Entity e) {
        Vec3 eyes = mc.player.getEyePosition();
        Vec3 target = new Vec3(e.getX(), e.getY() + e.getBbHeight() * 0.5, e.getZ());
        double dx = target.x - eyes.x;
        double dy = target.y - eyes.y;
        double dz = target.z - eyes.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.toDegrees(Math.atan2(-dx, dz)));
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, dist)));
        return new float[]{yaw, pitch};
    }

    public static void face(Entity e) {
        float[] r = getRotations(e);
        mc.player.setYRot(r[0]);
        mc.player.setXRot(Mth.clamp(r[1], -90, 90));
    }

    public static boolean isValidTarget(Entity e, double range, boolean players, boolean mobs, boolean animals, boolean invis) {
        if (!(e instanceof LivingEntity le)) return false;
        if (e == mc.player) return false;
        if (!e.isAlive()) return false;
        if (mc.player.distanceTo(e) > range) return false;
        if (e instanceof Player) {
            if (!players) return false;
            if (e.isInvisible() && !invis) return false;
        }
        return true;
    }
}
