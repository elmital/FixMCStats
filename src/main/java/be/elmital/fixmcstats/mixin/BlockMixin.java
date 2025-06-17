package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.WallRedstoneTorchBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.WoodType;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("all")
@Mixin(Block.class)
public class BlockMixin {
    // Fix https://bugs.mojang.com/browse/MC-245962
    @ModifyArg(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"))
    private Stat<?> modifyStat(Stat<?> stat) {
        if (!Configs.TIMES_MINED_BLOCKS_FIX.isActive())
            return stat;

        if ((Object) this instanceof WallTorchBlock wallTorchBlock)
            return Stats.MINED.getOrCreateStat(wallTorchBlock.getDefaultState().getBlock().equals(Blocks.SOUL_WALL_TORCH) ? Blocks.SOUL_TORCH : Blocks.TORCH);
        else if ((Object) this instanceof WallRedstoneTorchBlock)
            return Stats.MINED.getOrCreateStat(Blocks.REDSTONE_TORCH);
        else if ((Object) this instanceof WallSignBlock wallSignBlock) {
            if (wallSignBlock.getWoodType().equals(WoodType.OAK))
                return Stats.MINED.getOrCreateStat(Blocks.OAK_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.SPRUCE))
                return Stats.MINED.getOrCreateStat(Blocks.SPRUCE_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.BIRCH))
                return Stats.MINED.getOrCreateStat(Blocks.BIRCH_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.ACACIA))
                return Stats.MINED.getOrCreateStat(Blocks.ACACIA_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.CHERRY))
                return Stats.MINED.getOrCreateStat(Blocks.CHERRY_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.JUNGLE))
                return Stats.MINED.getOrCreateStat(Blocks.JUNGLE_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.DARK_OAK))
                return Stats.MINED.getOrCreateStat(Blocks.DARK_OAK_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.PALE_OAK))
                return Stats.MINED.getOrCreateStat(Blocks.PALE_OAK_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.CRIMSON))
                return Stats.MINED.getOrCreateStat(Blocks.CRIMSON_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.WARPED))
                return Stats.MINED.getOrCreateStat(Blocks.WARPED_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.MANGROVE))
                return Stats.MINED.getOrCreateStat(Blocks.MANGROVE_SIGN);
            else if (wallSignBlock.getWoodType().equals(WoodType.BAMBOO))
                return Stats.MINED.getOrCreateStat(Blocks.BAMBOO_SIGN);
        } else if ((Object) this instanceof WallBannerBlock wallBannerBlock) {
            if (wallBannerBlock.getColor().equals(DyeColor.WHITE))
                return Stats.MINED.getOrCreateStat(Blocks.WHITE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.ORANGE))
                return Stats.MINED.getOrCreateStat(Blocks.ORANGE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.MAGENTA))
                return Stats.MINED.getOrCreateStat(Blocks.MAGENTA_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.LIGHT_BLUE))
                return Stats.MINED.getOrCreateStat(Blocks.LIGHT_BLUE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.YELLOW))
                return Stats.MINED.getOrCreateStat(Blocks.YELLOW_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.LIME))
                return Stats.MINED.getOrCreateStat(Blocks.LIME_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.PINK))
                return Stats.MINED.getOrCreateStat(Blocks.PINK_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.GRAY))
                return Stats.MINED.getOrCreateStat(Blocks.GRAY_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.LIGHT_GRAY))
                return Stats.MINED.getOrCreateStat(Blocks.LIGHT_GRAY_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.CYAN))
                return Stats.MINED.getOrCreateStat(Blocks.CYAN_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.PURPLE))
                return Stats.MINED.getOrCreateStat(Blocks.PURPLE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.BLUE))
                return Stats.MINED.getOrCreateStat(Blocks.BLUE_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.BROWN))
                return Stats.MINED.getOrCreateStat(Blocks.BROWN_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.GREEN))
                return Stats.MINED.getOrCreateStat(Blocks.GREEN_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.RED))
                return Stats.MINED.getOrCreateStat(Blocks.RED_BANNER);
            else if (wallBannerBlock.getColor().equals(DyeColor.BLACK))
                return Stats.MINED.getOrCreateStat(Blocks.BLACK_BANNER);
        } else if ((Object) this instanceof WallSkullBlock wallSkullBlock) {
            if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.SKELETON))
                return Stats.MINED.getOrCreateStat(Blocks.SKELETON_SKULL);
            else if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.WITHER_SKELETON))
                return Stats.MINED.getOrCreateStat(Blocks.WITHER_SKELETON_SKULL);
            else if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.PLAYER))
                return Stats.MINED.getOrCreateStat(Blocks.PLAYER_HEAD);
            else if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.ZOMBIE))
                return Stats.MINED.getOrCreateStat(Blocks.ZOMBIE_HEAD);
            else if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.CREEPER))
                return Stats.MINED.getOrCreateStat(Blocks.CREEPER_HEAD);
            else if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.PIGLIN))
                return Stats.MINED.getOrCreateStat(Blocks.PIGLIN_HEAD);
            else if (wallSkullBlock.getSkullType().equals(SkullBlock.Type.DRAGON))
                return Stats.MINED.getOrCreateStat(Blocks.DRAGON_HEAD);
        } else if ((Object) this instanceof LeveledCauldronBlock) {
            return Stats.MINED.getOrCreateStat(Blocks.CAULDRON);
        }
        return stat;
    }
}
