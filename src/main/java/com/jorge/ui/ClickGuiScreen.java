package com.jorge.ui;

import com.jorge.core.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import java.util.*;

public class ClickGuiScreen extends Screen {
    private static final int PANEL_W = 110;
    private static final int HEADER_H = 16;
    private static final int MOD_H = 14;
    private final List<Panel> panels = new ArrayList<>();
    private Panel dragging = null;
    private int dragX, dragY;

    public ClickGuiScreen() {
        super(Component.literal("Jorge Client"));
        int x = 20;
        for (Category c : Category.values()) {
            panels.add(new Panel(c, x, 20));
            x += PANEL_W + 8;
        }
    }

    @Override public boolean isPauseScreen() { return false; }

    @Override
    public void extractRenderState(GuiGraphicsExtractor ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0x80000000);
        ctx.centeredText(font, "JORGE CLIENT  v2.0  |  RSHIFT to close", width / 2, 6, 0xFF55FFFF);
        for (Panel p : panels) p.render(ctx, mouseX, mouseY);
        super.extractRenderState(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean dbl) {
        double mx = e.x(), my = e.y();
        int btn = e.button();
        for (Panel p : panels) {
            if (mx >= p.x && mx <= p.x + PANEL_W && my >= p.y && my <= p.y + HEADER_H) {
                if (btn == InputConstants.MOUSE_BUTTON_LEFT) { dragging = p; dragX = (int)(mx - p.x); dragY = (int)(my - p.y); return true; }
            }
            int y = p.y + HEADER_H;
            for (HackModule m : ModuleManager.INSTANCE.byCategory(p.cat)) {
                if (mx >= p.x && mx <= p.x + PANEL_W && my >= y && my <= y + MOD_H) {
                    if (btn == InputConstants.MOUSE_BUTTON_LEFT) { m.toggle(); return true; }
                    if (btn == InputConstants.MOUSE_BUTTON_RIGHT) { p.expanded.toggle(m); return true; }
                }
                y += MOD_H;
                if (p.expanded.isOpen(m)) {
                    for (Setting<?> s : m.getSettings()) {
                        if (!s.isVisible()) continue;
                        int h = settingHeight(s);
                        if (mx >= p.x && mx <= p.x + PANEL_W && my >= y && my <= y + h) {
                            handleSettingClick(s, mx, p.x);
                            return true;
                        }
                        y += h;
                    }
                }
            }
        }
        return super.mouseClicked(e, dbl);
    }

    @Override public boolean mouseReleased(MouseButtonEvent e) {
        dragging = null;
        for (Panel p : panels) p.draggingSetting = null;
        return super.mouseReleased(e);
    }

    @Override public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
        double mx = e.x(), my = e.y();
        int btn = e.button();
        if (dragging != null && btn == InputConstants.MOUSE_BUTTON_LEFT) {
            dragging.x = (int)mx - dragX;
            dragging.y = (int)my - dragY;
            return true;
        }
        for (Panel p : panels) {
            if (p.draggingSetting instanceof NumberSetting n) {
                double rel = (mx - p.x - 4) / (PANEL_W - 8);
                rel = Math.max(0, Math.min(1, rel));
                n.set(n.getMin() + rel * (n.getMax() - n.getMin()));
                return true;
            }
            int y = p.y + HEADER_H;
            for (HackModule m : ModuleManager.INSTANCE.byCategory(p.cat)) {
                y += MOD_H;
                if (p.expanded.isOpen(m)) {
                    for (Setting<?> s : m.getSettings()) {
                        if (!s.isVisible()) continue;
                        int h = settingHeight(s);
                        if (s instanceof NumberSetting && mx >= p.x && mx <= p.x + PANEL_W && my >= y && my <= y + h && btn == InputConstants.MOUSE_BUTTON_LEFT) {
                            p.draggingSetting = s;
                            return true;
                        }
                        y += h;
                    }
                }
            }
        }
        return super.mouseDragged(e, dx, dy);
    }

    @Override public boolean keyPressed(KeyEvent e) {
        if (e.key() == InputConstants.KEY_ESCAPE || e.key() == InputConstants.KEY_RSHIFT) {
            minecraft.gui.setScreen(null);
            return true;
        }
        return super.keyPressed(e);
    }

    @Override public boolean mouseScrolled(double mx, double my, double sx, double sy) {
        for (Panel p : panels) {
            if (mx >= p.x && mx <= p.x + PANEL_W && my >= p.y) { p.scroll -= (int)(sy * 8); return true; }
        }
        return super.mouseScrolled(mx, my, sx, sy);
    }

    private int settingHeight(Setting<?> s) {
        if (s instanceof NumberSetting) return 16;
        return 12;
    }

    private void handleSettingClick(Setting<?> s, double mx, int px) {
        if (s instanceof BooleanSetting b) b.toggle();
        else if (s instanceof ModeSetting md) md.cycle();
        else if (s instanceof NumberSetting n) {
            double rel = (mx - px - 4) / (PANEL_W - 8);
            n.set(n.getMin() + Math.max(0, Math.min(1, rel)) * (n.getMax() - n.getMin()));
        }
    }

    /** Little 8x8 checkbox: cyan + white tick when on, dark + gray border when off. */
    private void drawCheckBox(GuiGraphicsExtractor ctx, int bx, int by, boolean on) {
        if (on) {
            ctx.fill(bx, by, bx + 8, by + 8, 0xFF55FFFF);
            ctx.fill(bx, by, bx + 8, by + 1, 0xFF9BFFFF);
            ctx.fill(bx + 1, by + 4, bx + 3, by + 6, 0xFF101018);
            ctx.fill(bx + 3, by + 2, bx + 7, by + 4, 0xFF101018);
        } else {
            ctx.fill(bx, by, bx + 8, by + 8, 0xFF000000);
            ctx.fill(bx, by, bx + 8, by + 1, 0xFF555555);
            ctx.fill(bx, by + 7, bx + 8, by + 8, 0xFF555555);
            ctx.fill(bx, by, bx + 1, by + 8, 0xFF555555);
            ctx.fill(bx + 7, by, bx + 8, by + 8, 0xFF555555);
        }
    }

    private class Panel {
        Category cat; int x, y; int scroll = 0;
        ExpandedState expanded = new ExpandedState();
        Setting<?> draggingSetting = null;
        Panel(Category c, int x, int y) { this.cat = c; this.x = x; this.y = y; }
        void render(GuiGraphicsExtractor ctx, int mouseX, int mouseY) {
            ctx.fill(x, y, x + PANEL_W, y + HEADER_H, 0xFF101018);
            ctx.fill(x, y + HEADER_H - 2, x + PANEL_W, y + HEADER_H, 0xFF55FFFF);
            ctx.text(font, cat.display.toUpperCase(), x + 6, y + 4, 0xFFFFFFFF, true);
            int yy = y + HEADER_H - scroll;
            for (HackModule mod : ModuleManager.INSTANCE.byCategory(cat)) {
                boolean hover = mouseX >= x && mouseX <= x + PANEL_W && mouseY >= yy && mouseY <= yy + MOD_H && yy >= y + HEADER_H;
                int bg = mod.isEnabled() ? 0xFF00AAAA : 0xFF1A1A22;
                if (hover) bg = mod.isEnabled() ? 0xFF00CCCC : 0xFF2A2A35;
                if (yy >= y + HEADER_H - 4) {
                    ctx.fill(x, yy, x + PANEL_W, yy + MOD_H, bg);
                    drawCheckBox(ctx, x + 3, yy + 3, mod.isEnabled());
                    ctx.text(font, mod.getName() + (mod.getSuffix().isEmpty() ? "" : " | " + mod.getSuffix()), x + 14, yy + 3, 0xFFFFFFFF, true);
                    if (expanded.isOpen(mod)) {
                        ctx.fill(x + PANEL_W - 10, yy + 3, x + PANEL_W - 4, yy + 5, 0xFF55FFFF);
                    }
                }
                yy += MOD_H;
                if (expanded.isOpen(mod)) {
                    for (Setting<?> s : mod.getSettings()) {
                        if (!s.isVisible()) continue;
                        int h = settingHeight(s);
                        if (yy >= y + HEADER_H - 4) {
                            ctx.fill(x, yy, x + PANEL_W, yy + h, 0xFF22222C);
                            if (s instanceof BooleanSetting b) {
                                drawCheckBox(ctx, x + 6, yy + 2, b.isOn());
                                ctx.text(font, s.getName(), x + 18, yy + 2, 0xFFFFFFFF, true);
                            } else if (s instanceof ModeSetting md) {
                                ctx.text(font, s.getName() + ": " + md.get(), x + 6, yy + 2, 0xFFFFFFFF, true);
                            } else if (s instanceof NumberSetting n) {
                                ctx.text(font, s.getName() + ": " + String.format("%.2f", n.get()), x + 6, yy + 1, 0xFFFFFFFF, true);
                                ctx.fill(x + 4, yy + 11, x + PANEL_W - 4, yy + 13, 0xFF000000);
                                double rel = (n.get() - n.getMin()) / Math.max(0.001, n.getMax() - n.getMin());
                                ctx.fill(x + 4, yy + 11, (int)(x + 4 + rel * (PANEL_W - 8)), yy + 13, 0xFF55FFFF);
                            }
                        }
                        yy += h;
                    }
                }
            }
        }
    }

    private static class ExpandedState {
        private final Set<HackModule> open = new HashSet<>();
        void toggle(HackModule m) { if (!open.remove(m)) open.add(m); }
        boolean isOpen(HackModule m) { return open.contains(m); }
    }
}
