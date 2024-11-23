package be.elmital.fixmcstats.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {
    // Fix https://bugs.mojang.com/browse/MC-121541
    @Inject(method = "onLandedUpon", at = @At(value = "HEAD"))
    private void incrementOnLanding(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        if (fallDistance >= 2.0F && entity instanceof ServerPlayerEntity player) {
            player.increaseStat(Stats.FALL_ONE_CM, (int)Math.round((double)fallDistance * 100.0));
        }
    }
}
