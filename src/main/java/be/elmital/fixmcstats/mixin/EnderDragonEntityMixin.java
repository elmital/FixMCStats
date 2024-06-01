package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Config;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

// Experimental fix for https://bugs.mojang.com/browse/MC-267006
@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {
    @Redirect(method = "launchLivingEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void increaseVelocity(Entity entity, double deltaX, double deltaY, double deltaZ) {
        if (Config.instance().FIX_ENDER_DRAGON_FLOWN_STAT) {
            if (!(entity instanceof PlayerEntity))
                entity.addVelocity(deltaX, deltaY, deltaZ);
        } else {
            entity.addVelocity(deltaX, deltaY, deltaZ);
        }
    }

    @Inject(method = "launchLivingEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public void increaseVelocityPostDamageCheck(ServerWorld world, List<Entity> entities, CallbackInfo ci, @Local Entity entity, @Local(ordinal = 2) double f, @Local(ordinal = 3) double g, @Local(ordinal = 4) double h) {
        if (Config.instance().FIX_ENDER_DRAGON_FLOWN_STAT && entity instanceof PlayerEntity) entity.addVelocity(f / h * 4.0, 0.2f, g / h * 4.0);
    }
}