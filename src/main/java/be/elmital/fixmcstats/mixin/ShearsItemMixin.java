package be.elmital.fixmcstats.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    // Fix https://bugs.mojang.com/browse/MC-128079
    @Inject(method = "postMine", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"))
    private void incrementUseStat(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        if (miner instanceof PlayerEntity player && (!state.isIn(BlockTags.LEAVES) && !state.isOf(Blocks.COBWEB) && !state.isOf(Blocks.SHORT_GRASS) && !state.isOf(Blocks.FERN) && !state.isOf(Blocks.DEAD_BUSH) && !state.isOf(Blocks.HANGING_ROOTS) && !state.isOf(Blocks.VINE) && !state.isOf(Blocks.TRIPWIRE) && !state.isIn(BlockTags.WOOL)))
            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
    }
}
