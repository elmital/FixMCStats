package be.elmital.fixmcstats.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {
    // Fix https://bugs.mojang.com/browse/MC-121541
    @Inject(method = "fallOn", at = @At(value = "HEAD"))
    private void incrementOnLanding(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        if (fallDistance >= 2.0F && entity instanceof ServerPlayer player) {
            player.awardStat(Stats.FALL_ONE_CM, (int)Math.round((double)fallDistance * 100.0));
        }
    }
}
