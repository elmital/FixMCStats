package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    // Fix https://bugs.mojang.com/browse/MC-111435
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private void incrementSweepDamage(Entity target, CallbackInfo ci, @Local(ordinal = 4) float damage) {
        ((Player) (Object) this).awardStat(Stats.DAMAGE_DEALT, Math.round((damage - ((LivingEntity) target).getHealth()) * 10.0f));
    }
}
