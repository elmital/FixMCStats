package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(SnowballEntity.class)
public abstract class SnowBallEntityMixin extends ThrownItemEntity {
    public SnowBallEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    // Fix https://bugs.mojang.com/browse/MC-29519
    @Inject(method = "onEntityHit", at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;serverDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", shift = At.Shift.AFTER))
    private void increaseDamageStat(EntityHitResult entityHitResult, CallbackInfo ci, @Local int damages) {
        if (getOwner() instanceof PlayerEntity player && damages > 0) {
            player.increaseStat(Stats.DAMAGE_DEALT, Math.round(damages * 10.0F));
        }
    }
}
