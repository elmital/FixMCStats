package be.elmital.fixmcstats.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow public double fallDistance;

    // Fix https://bugs.mojang.com/browse/MC-121541 fix for water and cobweb
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;resetFallDistance()V"))
    private void onLanding(CallbackInfo ci) {
        if (fallDistance >= 2.0 && (Object) this instanceof ServerPlayer player)
            player.awardStat(Stats.FALL_ONE_CM, (int)Math.round(fallDistance * 100.0));
    }
}
