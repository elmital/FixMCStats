package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Fix https://bugs.mojang.com/browse/MC-211938
    @WrapWithCondition(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"))
    public boolean incrementStat(PlayerEntity instance, Identifier stat) {
        if (instance.getBlockStateAtPos().getBlock() instanceof ScaffoldingBlock)
            return !Configs.JUMP_WHEN_CLIMBING_SCAFFOLDING_FIX.isActive();
        return true;
    }

    // Fix https://bugs.mojang.com/browse/MC-111435
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private void incrementSweepDamage(Entity target, CallbackInfo ci, @Local(ordinal = 4) float damage) {
        if (Configs.DAMAGE_DEALT_WITH_SWEEPING_FIX.isActive())
            ((PlayerEntity) (Object) this).increaseStat(Stats.DAMAGE_DEALT, Math.round((damage - ((LivingEntity)target).getHealth()) * 10.0f));
    }
}
