package com.jorge.mixin;

import com.jorge.core.ModuleManager;
import com.jorge.modules.combat.Velocity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class)
public class MixinEntity {
    @Inject(method = "push(DDD)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void jorge$velocity(double dx, double dy, double dz, CallbackInfo ci) {
        try {
            if (!(((Object) this) instanceof LocalPlayer self)) return;
            Velocity v = ModuleManager.INSTANCE.get(Velocity.class);
            if (v == null || !v.isEnabled()) return;
            if (v.mode.is("Cancel")) {
                ci.cancel();
            } else if (v.mode.is("Reduce")) {
                double h = v.horizontal.get() / 100.0;
                double vv = v.vertical.get() / 100.0;
                var d = self.getDeltaMovement();
                self.setDeltaMovement(d.x + dx * h, d.y + dy * vv, d.z + dz * h);
                ci.cancel();
            }
        } catch (Throwable ignored) {}
    }
}
