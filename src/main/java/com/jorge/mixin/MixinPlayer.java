package com.jorge.mixin;

import com.jorge.core.ModuleManager;
import com.jorge.modules.combat.Reach;
import com.jorge.modules.player.FastBreak;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "entityInteractionRange", at = @At("HEAD"), cancellable = true, require = 0)
    private void jorge$reachEntity(CallbackInfoReturnable<Double> cir) {
        double r = Reach.getReach();
        if (r > 3.0) cir.setReturnValue(r);
    }

    @Inject(method = "blockInteractionRange", at = @At("HEAD"), cancellable = true, require = 0)
    private void jorge$reachBlock(CallbackInfoReturnable<Double> cir) {
        double r = Reach.getReach();
        if (r > 3.0) cir.setReturnValue(r);
    }

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true, require = 0)
    private void jorge$fastbreak(BlockState state, CallbackInfoReturnable<Float> cir) {
        try {
            FastBreak fb = ModuleManager.INSTANCE.get(FastBreak.class);
            if (fb != null && fb.isEnabled()) cir.setReturnValue(cir.getReturnValue() * fb.mult.getFloat());
        } catch (Throwable ignored) {}
    }
}
