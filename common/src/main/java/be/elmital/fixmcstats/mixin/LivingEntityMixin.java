package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements Attackable, WaypointTransmitter {
    @Shadow public abstract boolean isDeadOrDying();

    @Shadow public abstract float getHealth();

    // Fix https://bugs.mojang.com/browse/MC-29519
    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getCombatTracker()Lnet/minecraft/world/damagesource/CombatTracker;", shift = At.Shift.AFTER))
    public void incrementDamageDealtStatForProjectile(ServerLevel world, DamageSource source, float amount, CallbackInfo ci) {
        if (Configs.DAMAGE_DEALT_WITH_PROJECTILE_FIX.isActive() && (source.is(DamageTypeTags.IS_PROJECTILE) || source.getDirectEntity() instanceof FireworkRocketEntity) && source.getEntity() instanceof ServerPlayer player) {
            player.awardStat(Stats.DAMAGE_DEALT,  Math.round(Math.min(this.getHealth(), amount) * 10.0F));
        }
    }

    // Fix https://bugs.mojang.com/browse/MC-265376
    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 5))
    public void onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Configs.KILLED_BY_GOAT_FIX.isActive() && (Object) this instanceof ServerPlayer pl && isDeadOrDying() && source.getEntity() instanceof Goat goat)
            pl.awardStat(Stats.ENTITY_KILLED_BY.get(goat.getType()), 1);
    }
}
