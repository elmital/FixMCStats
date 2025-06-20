package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow public abstract boolean isDead();

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract float getHealth();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Fix https://bugs.mojang.com/browse/MC-122656
    @Inject(method = "tickGliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V", shift = At.Shift.AFTER))
    public void incrementBreakingStat(CallbackInfo ci, @Local EquipmentSlot slot) {
        if (!Configs.BREAKING_ELYTRA_AND_TRIDENT_FIX.isActive())
            return;
        ItemStack equiped = this.getEquippedStack(slot);
        if (equiped.willBreakNextUse()) {
            if (((LivingEntity) (Object) this) instanceof PlayerEntity playerEntity)
                playerEntity.incrementStat(Stats.BROKEN.getOrCreateStat(equiped.getItem()));
        }
    }

    // Fix https://bugs.mojang.com/browse/MC-29519
    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTracker()Lnet/minecraft/entity/damage/DamageTracker;", shift = At.Shift.AFTER))
    public void incrementDamageDealtStatForProjectile(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        if (Configs.DAMAGE_DEALT_WITH_PROJECTILE_FIX.isActive() && (source.isIn(DamageTypeTags.IS_PROJECTILE) || source.getSource() instanceof FireworkRocketEntity) && source.getAttacker() instanceof ServerPlayerEntity player) {
            player.increaseStat(Stats.DAMAGE_DEALT,  Math.round(Math.min(this.getHealth(), amount) * 10.0F));
        }
    }

    // Fix https://bugs.mojang.com/browse/MC-265376
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 5))
    public void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (Configs.KILLED_BY_GOAT_FIX.isActive() && (Object) this instanceof ServerPlayerEntity pl && isDead() && source.getAttacker() instanceof GoatEntity goat)
            pl.increaseStat(Stats.KILLED_BY.getOrCreateStat(goat.getType()), 1);
    }
}
