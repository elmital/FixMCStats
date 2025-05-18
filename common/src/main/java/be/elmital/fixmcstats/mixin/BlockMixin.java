package be.elmital.fixmcstats.mixin;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("all")
@Mixin(value = Block.class)
public class BlockMixin {
    // Fix https://bugs.mojang.com/browse/MC-245962
    @ModifyArg(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    private Stat<?> modifyStat(Stat<?> stat) {
        if ((Object) this instanceof WallTorchBlock wallTorchBlock)
            return Stats.BLOCK_MINED.get(wallTorchBlock.defaultBlockState().getBlock().equals(Blocks.SOUL_WALL_TORCH) ? Blocks.SOUL_TORCH : Blocks.TORCH);
        else if ((Object) this instanceof RedstoneWallTorchBlock)
            return Stats.BLOCK_MINED.get(Blocks.REDSTONE_TORCH);
        else if ((Object) this instanceof WallSignBlock wallSignBlock) {
            if (wallSignBlock.type().equals(WoodType.OAK))
                return Stats.BLOCK_MINED.get(Blocks.OAK_SIGN);
            else if (wallSignBlock.type().equals(WoodType.SPRUCE))
                return Stats.BLOCK_MINED.get(Blocks.SPRUCE_SIGN);
            else if (wallSignBlock.type().equals(WoodType.BIRCH))
                return Stats.BLOCK_MINED.get(Blocks.BIRCH_SIGN);
            else if (wallSignBlock.type().equals(WoodType.ACACIA))
                return Stats.BLOCK_MINED.get(Blocks.ACACIA_SIGN);
            else if (wallSignBlock.type().equals(WoodType.CHERRY))
                return Stats.BLOCK_MINED.get(Blocks.CHERRY_SIGN);
            else if (wallSignBlock.type().equals(WoodType.JUNGLE))
                return Stats.BLOCK_MINED.get(Blocks.JUNGLE_SIGN);
            else if (wallSignBlock.type().equals(WoodType.DARK_OAK))
                return Stats.BLOCK_MINED.get(Blocks.DARK_OAK_SIGN);
            else if (wallSignBlock.type().equals(WoodType.PALE_OAK))
                return Stats.BLOCK_MINED.get(Blocks.PALE_OAK_SIGN);
            else if (wallSignBlock.type().equals(WoodType.CRIMSON))
                return Stats.BLOCK_MINED.get(Blocks.CRIMSON_SIGN);
            else if (wallSignBlock.type().equals(WoodType.WARPED))
                return Stats.BLOCK_MINED.get(Blocks.WARPED_SIGN);
            else if (wallSignBlock.type().equals(WoodType.MANGROVE))
                return Stats.BLOCK_MINED.get(Blocks.MANGROVE_SIGN);
            else if (wallSignBlock.type().equals(WoodType.BAMBOO))
                return Stats.BLOCK_MINED.get(Blocks.BAMBOO_SIGN);
        } else if ((Object) this instanceof WallBannerBlock wallBannerBlock) {
            if (wallBannerBlock.getColor().equals(DyeColor.WHITE))
                return Stats.BLOCK_MINED.get(Blocks.WHITE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.ORANGE))
                return Stats.BLOCK_MINED.get(Blocks.ORANGE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.MAGENTA))
                return Stats.BLOCK_MINED.get(Blocks.MAGENTA_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.LIGHT_BLUE))
                return Stats.BLOCK_MINED.get(Blocks.LIGHT_BLUE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.YELLOW))
                return Stats.BLOCK_MINED.get(Blocks.YELLOW_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.LIME))
                return Stats.BLOCK_MINED.get(Blocks.LIME_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.PINK))
                return Stats.BLOCK_MINED.get(Blocks.PINK_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.GRAY))
                return Stats.BLOCK_MINED.get(Blocks.GRAY_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.LIGHT_GRAY))
                return Stats.BLOCK_MINED.get(Blocks.LIGHT_GRAY_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.CYAN))
                return Stats.BLOCK_MINED.get(Blocks.CYAN_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.PURPLE))
                return Stats.BLOCK_MINED.get(Blocks.PURPLE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.BLUE))
                return Stats.BLOCK_MINED.get(Blocks.BLUE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.BROWN))
                return Stats.BLOCK_MINED.get(Blocks.BROWN_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.GREEN))
                return Stats.BLOCK_MINED.get(Blocks.GREEN_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.RED))
                return Stats.BLOCK_MINED.get(Blocks.RED_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.BLACK))
                return Stats.BLOCK_MINED.get(Blocks.BLACK_BANNER);
        } else if ((Object) this instanceof WallSkullBlock wallSkullBlock) {
            if (wallSkullBlock.getType().equals(SkullBlock.Types.SKELETON))
                return Stats.BLOCK_MINED.get(Blocks.SKELETON_SKULL);
            else if (wallSkullBlock.getType().equals(SkullBlock.Types.WITHER_SKELETON))
                return Stats.BLOCK_MINED.get(Blocks.WITHER_SKELETON_SKULL);
            else if (wallSkullBlock.getType().equals(SkullBlock.Types.PLAYER))
                return Stats.BLOCK_MINED.get(Blocks.PLAYER_HEAD);
            else if (wallSkullBlock.getType().equals(SkullBlock.Types.ZOMBIE))
                return Stats.BLOCK_MINED.get(Blocks.ZOMBIE_HEAD);
            else if (wallSkullBlock.getType().equals(SkullBlock.Types.CREEPER))
                return Stats.BLOCK_MINED.get(Blocks.CREEPER_HEAD);
            else if (wallSkullBlock.getType().equals(SkullBlock.Types.PIGLIN))
                return Stats.BLOCK_MINED.get(Blocks.PIGLIN_HEAD);
            else if (wallSkullBlock.getType().equals(SkullBlock.Types.DRAGON))
                return Stats.BLOCK_MINED.get(Blocks.DRAGON_HEAD);
        } else if ((Object) this instanceof LayeredCauldronBlock) {
            return Stats.BLOCK_MINED.get(Blocks.CAULDRON);
        }
        return stat;
    }
}
