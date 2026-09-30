package com.jorge.ui;

import com.jorge.core.HackModule;
import com.jorge.core.ModuleManager;
import com.jorge.modules.render.*;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives.Group;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives.Line;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HudOverlay {
    static final Minecraft mc = Minecraft.getInstance();

    public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath("jorge-client", "hud"), HudOverlay::renderHud);
        LevelRenderEvents.BEFORE_GIZMOS.register(HudOverlay::renderWorld);
    }

    private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (mc.player == null) return;
        HUD hud = ModuleManager.INSTANCE.get(HUD.class);
        if (hud != null && !hud.isEnabled()) return;
        boolean watermark = hud == null || hud.watermark.isOn();
        boolean arraylist = hud == null || hud.arraylist.isOn();
        boolean coords = hud == null || hud.coords.isOn();
        boolean fps = hud == null || hud.fps.isOn();

        if (watermark) {
            graphics.fill(4, 4, 122, 22, 0xAA101018);
            graphics.fill(4, 20, 122, 22, 0xFF55FFFF);
            graphics.text(mc.font, "JORGE CLIENT v2.0", 10, 9, 0xFFFFFFFF, true);
        }
        int y = 28;
        if (fps) {
            graphics.text(mc.font, mc.getFps() + " FPS", 6, y, 0xFFAAAAAA, true);
            y += 10;
        }
        if (coords && mc.player != null) {
            graphics.text(mc.font, String.format("%.1f / %.1f / %.1f",
                mc.player.getX(), mc.player.getY(), mc.player.getZ()), 6, y, 0xFFAAAAAA, true);
            y += 10;
        }
        if (arraylist) {
            var mods = ModuleManager.INSTANCE.getAll().stream()
                .filter(HackModule::isEnabled)
                .filter(m -> !(m instanceof HUD))
                .sorted(Comparator.comparingInt(m -> -mc.font.width(m.getName())))
                .toList();
            int sw = graphics.guiWidth();
            int ry = 4;
            for (HackModule m : mods) {
                String t = m.getName() + (m.getSuffix().isEmpty() ? "" : " " + m.getSuffix());
                int w = mc.font.width(t);
                int rx = sw - w - 8;
                graphics.fill(rx - 2, ry, sw - 2, ry + 11, 0xAA101018);
                graphics.fill(sw - 2, ry, sw, ry + 11, 0xFF55FFFF);
                graphics.text(mc.font, t, rx, ry + 2, 0xFFFFFFFF, true);
                ry += 12;
            }
        }
    }

    private static void renderWorld(LevelRenderContext ctx) {
        if (mc.level == null || mc.player == null) return;
        List<Line> lines = new ArrayList<>();

        ESP esp = ModuleManager.INSTANCE.get(ESP.class);
        Tracers tracers = ModuleManager.INSTANCE.get(Tracers.class);
        boolean boxOn = esp != null && esp.isEnabled() && esp.mode.is("Box");
        boolean tracerOn = tracers != null && tracers.isEnabled();
        if (boxOn || tracerOn) {
            Vec3 eyes = mc.player.getEyePosition();
            for (Entity e : mc.level.entitiesForRendering()) {
                if (e == mc.player || !(e instanceof LivingEntity)) continue;
                boolean isPlayer = e instanceof Player;
                if (boxOn) {
                    if (isPlayer && !esp.players.isOn()) continue;
                    if (!isPlayer && !esp.mobs.isOn()) continue;
                }
                if (mc.player.distanceTo(e) > 64) continue;
                int color = isPlayer ? 0xFFFF3030 : 0xFF30FF30;
                if (boxOn) boxLines(lines, e.getBoundingBox(), color);
                if (tracerOn) {
                    Vec3 mid = e.position().add(0, e.getBbHeight() / 2, 0);
                    lines.add(new Line(eyes, mid, color, 1.0f));
                }
            }
        }

        StorageESP storage = ModuleManager.INSTANCE.get(StorageESP.class);
        if (storage != null && storage.isEnabled()) {
            BlockPos origin = mc.player.blockPosition();
            for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-30, -15, -30), origin.offset(30, 15, 30))) {
                var be = mc.level.getBlockEntity(pos);
                if (be == null) continue;
                int color = -1;
                if (be instanceof ChestBlockEntity && storage.chest.isOn()) color = 0xFFFFAA00;
                else if (be instanceof EnderChestBlockEntity && storage.ender.isOn()) color = 0xFFFFAA00;
                else if (be instanceof ShulkerBoxBlockEntity && storage.shulker.isOn()) color = 0xFFB42FFF;
                if (color != -1) boxLines(lines, new AABB(pos), color);
            }
        }

        ItemESP itemEsp = ModuleManager.INSTANCE.get(ItemESP.class);
        if (itemEsp != null && itemEsp.isEnabled()) {
            for (Entity e : mc.level.entitiesForRendering()) {
                if (e instanceof ItemEntity) boxLines(lines, e.getBoundingBox(), 0xFFFFFF30);
            }
        }

        XRay xray = ModuleManager.INSTANCE.get(XRay.class);
        if (xray != null && xray.isEnabled()) {
            for (XRay.Spot s : XRay.SPOTS) {
                if (xray.show(s.key())) boxLines(lines, new AABB(s.pos()), s.color());
            }
        }

        if (lines.isEmpty()) return;
        ctx.submitNodeCollector().submitGizmoPrimitives(
            new Group(false, lines, List.of(), List.of(), List.of(), List.of()),
            ctx.levelState().cameraRenderState, false);
    }

    private static void boxLines(List<Line> out, AABB box, int color) {
        double x0 = box.minX, y0 = box.minY, z0 = box.minZ;
        double x1 = box.maxX, y1 = box.maxY, z1 = box.maxZ;
        Vec3 b000 = new Vec3(x0, y0, z0), b001 = new Vec3(x0, y0, z1);
        Vec3 b010 = new Vec3(x0, y1, z0), b011 = new Vec3(x0, y1, z1);
        Vec3 b100 = new Vec3(x1, y0, z0), b101 = new Vec3(x1, y0, z1);
        Vec3 b110 = new Vec3(x1, y1, z0), b111 = new Vec3(x1, y1, z1);
        edge(out, b000, b001, color); edge(out, b001, b101, color);
        edge(out, b101, b100, color); edge(out, b100, b000, color);
        edge(out, b010, b011, color); edge(out, b011, b111, color);
        edge(out, b111, b110, color); edge(out, b110, b010, color);
        edge(out, b000, b010, color); edge(out, b001, b011, color);
        edge(out, b100, b110, color); edge(out, b101, b111, color);
    }

    private static void edge(List<Line> out, Vec3 a, Vec3 b, int color) {
        out.add(new Line(a, b, color, 1.0f));
    }
}
