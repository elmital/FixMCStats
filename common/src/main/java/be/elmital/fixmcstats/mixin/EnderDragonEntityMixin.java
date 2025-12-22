package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

// Experimental fix for https://bugs.mojang.com/browse/MC-267006
@Mixin(EnderDragon.class)
public class EnderDragonEntityMixin {
    @Redirect(method = "knockBack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;push(DDD)V"))
    public void increaseVelocity(Entity entity, double deltaX, double deltaY, double deltaZ) {
        if (Configs.ENDER_DRAGON_FLOWN_STAT_FIX.isActive()) {
            if (!(entity instanceof Player))
                entity.push(deltaX, deltaY, deltaZ);
        } else {
            entity.push(deltaX, deltaY, deltaZ);
        }
    }

    @Inject(method = "knockBack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    public void increaseVelocityPostDamageCheck(ServerLevel world, List<Entity> entities, CallbackInfo ci, @Local Entity entity, @Local(ordinal = 2) double f, @Local(ordinal = 3) double g, @Local(ordinal = 4) double h) {
        if (Configs.ENDER_DRAGON_FLOWN_STAT_FIX.isActive() && entity instanceof Player) entity.push(f / h * 4.0, 0.2f, g / h * 4.0);
    }
}