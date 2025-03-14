package be.elmital.fixmcstats.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {
    // Fix https://bugs.mojang.com/browse/MC-121541
    @Inject(method = "onLandedUpon", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;handleFallDamage(DFLnet/minecraft/entity/damage/DamageSource;)Z"))
    private void incrementOnLanding(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci) {
        if (fallDistance >= 2.0F && entity instanceof ServerPlayerEntity player) {
            player.increaseStat(Stats.FALL_ONE_CM, (int)Math.round(fallDistance * 100.0));
        }
    }
}
