package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlaceableOnWaterItem.class)
public class PlaceableOnWaterItemMixin extends BlockItem {
    public PlaceableOnWaterItemMixin(Block block, Settings settings) {
        super(block, settings);
    }

    // Fix https://bugs.mojang.com/browse/MC-264274
    @Inject(method = "use", at = @At(value = "RETURN"))
    public void incrementStat(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> result) {
        if (Configs.PLACEABLE_ON_WATER_FIX.isActive() && result.getReturnValue().getResult().shouldIncrementStat())
            user.incrementStat(Stats.USED.getOrCreateStat(this));
    }
}
