package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow public abstract boolean isDeadOrDying();

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Shadow public abstract float getHealth();

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    // Fix https://bugs.mojang.com/browse/MC-29519
    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getCombatTracker()Lnet/minecraft/world/damagesource/CombatTracker;", shift = At.Shift.AFTER))
    public void incrementDamageDealtStatForProjectile(DamageSource source, float amount, CallbackInfo ci) {
        if (Configs.DAMAGE_DEALT_WITH_PROJECTILE_FIX.isActive() && (source.is(DamageTypeTags.IS_PROJECTILE) || source.getDirectEntity() instanceof FireworkRocketEntity) && source.getEntity() instanceof ServerPlayer player) {
            player.awardStat(Stats.DAMAGE_DEALT,  Math.round(Math.min(this.getHealth(), amount) * 10.0F));
        }
    }

    // Fix https://bugs.mojang.com/browse/MC-265376
    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 5))
    public boolean onDamage(DamageSource source, TagKey<DamageType> tag) {
        return Configs.KILLED_BY_GOAT_FIX.isActive() &&  source.is(tag) && !(source.getEntity() instanceof Goat) && !isDeadOrDying();
    }
}
