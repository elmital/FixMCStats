package be.elmital.fixmcstats.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public float fallDistance;

    // Fix https://bugs.mojang.com/browse/MC-121541 fix for water and cobweb
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onLanding()V"))
    private void onLanding(CallbackInfo ci) {
        if (fallDistance >= 2.0F && (Object) this instanceof ServerPlayerEntity player)
            player.increaseStat(Stats.FALL_ONE_CM, (int)Math.round((double) fallDistance * 100.0));
    }
}
