package com.jorge.mixin;

import com.jorge.core.ModuleManager;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {
    @Shadow private int destroyDelay;

    @Inject(method = "tick", at = @At("HEAD"), require = 0)
    private void jorge$fastbreak(CallbackInfo ci) {
        try {
            var fb = ModuleManager.INSTANCE.get(com.jorge.modules.player.FastBreak.class);
            if (fb != null && fb.isEnabled()) destroyDelay = 0;
        } catch (Throwable ignored) {}
    }
}
