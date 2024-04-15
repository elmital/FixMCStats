package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    // Fix https://bugs.mojang.com/browse/MC-29519
    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 1))
    private void increaseDamageStat(CallbackInfo ci, @Local(ordinal = 1) float damages) {
        if (((FireworkRocketEntity) (Object) this).getOwner() instanceof ServerPlayerEntity playerEntity)
            playerEntity.increaseStat(Stats.DAMAGE_DEALT, Math.round(damages * 10.0f));
    }
}
