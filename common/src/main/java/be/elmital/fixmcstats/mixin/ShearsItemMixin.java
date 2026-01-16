package be.elmital.fixmcstats.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    // Fix https://bugs.mojang.com/browse/MC-128079
    @Inject(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
    private void incrementUseStat(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        if (miner instanceof Player player && (!state.is(BlockTags.LEAVES) && !state.is(Blocks.COBWEB) && !state.is(Blocks.SHORT_GRASS) && !state.is(Blocks.FERN) && !state.is(Blocks.DEAD_BUSH) && !state.is(Blocks.HANGING_ROOTS) && !state.is(Blocks.VINE) && !state.is(Blocks.TRIPWIRE) && !state.is(BlockTags.WOOL)))
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
    }
}
