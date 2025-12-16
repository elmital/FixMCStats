package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("all")
@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public abstract BlockState defaultBlockState();

    // Fix https://bugs.mojang.com/browse/MC-245962
    @ModifyArg(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    private Stat<?> modifyStat(Stat<?> stat) {
        if (!Configs.TIMES_MINED_BLOCKS_FIX.isActive())
            return stat;

        if ((Object) this instanceof WallTorchBlock || (Object) this instanceof RedstoneWallTorchBlock || (Object) this instanceof WallSignBlock || (Object) this instanceof WallBannerBlock || (Object) this instanceof WallSkullBlock || (Object) this instanceof CauldronBlock) {
            final var block = Block.byItem(defaultBlockState().getBlock().asItem());
            if (block.equals(Blocks.AIR))
                return stat;
            return Stats.BLOCK_MINED.get(block);
        }
        return stat;
    }
}
