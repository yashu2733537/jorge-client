package com.jorge.modules.movement;

import com.jorge.core.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.inventory.ContainerInput;

public class Scaffold extends HackModule {
    public final NumberSetting expand = add(new NumberSetting("Expand", 0.0, 0.0, 6.0, 0.5));
    public final BooleanSetting sprint = add(new BooleanSetting("Sprint", false));
    public final BooleanSetting tower = add(new BooleanSetting("Tower", true));
    public Scaffold() { super("Scaffold", "Places blocks under you", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;
        BlockPos below = mc.player.blockPosition().below();
        if (!mc.level.getBlockState(below).isAir()) return;
        // find block in hotbar
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getItem(i);
            if (stack.getItem() instanceof BlockItem) { slot = i; break; }
        }
        if (slot == -1) {
            // no blocks in hotbar: pull a stack in from main inventory
            if (mc.player.containerMenu == null) return;
            for (int i = 9; i < 36; i++) {
                var stack = mc.player.getInventory().getItem(i);
                if (stack.getItem() instanceof BlockItem) {
                    mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, i, 0, ContainerInput.QUICK_MOVE, mc.player);
                    return;
                }
            }
            return;
        }
        int prev = mc.player.getInventory().getSelectedSlot();
        mc.player.getInventory().setSelectedSlot(slot);
        Vec3 eyes = mc.player.getEyePosition();
        Vec3 center = new Vec3(below.getX() + 0.5, below.getY() + 0.5, below.getZ() + 0.5);
        Vec3 dir = center.subtract(eyes).normalize();
        // tower
        if (tower.isOn() && mc.options.keyJump.isDown() && mc.player.zza == 0) {
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.42, mc.player.getDeltaMovement().z);
        }
        BlockHitResult hit = new BlockHitResult(center, Direction.UP, below, false);
        mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
        mc.player.swing(InteractionHand.MAIN_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, false);
        mc.player.getInventory().setSelectedSlot(prev);
        if (!sprint.isOn()) mc.player.setSprinting(false);
        // face down for legit
        mc.player.setXRot(81f);
    }
}
