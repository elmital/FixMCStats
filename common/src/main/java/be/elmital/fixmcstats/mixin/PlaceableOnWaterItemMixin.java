package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlaceOnWaterBlockItem.class)
public class PlaceableOnWaterItemMixin extends BlockItem {
    public PlaceableOnWaterItemMixin(Block block, Properties settings) {
        super(block, settings);
    }

    // Fix https://bugs.mojang.com/browse/MC-264274
    @Inject(method = "use", at = @At(value = "RETURN"))
    public void incrementStat(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> result) {
        if (Configs.PLACEABLE_ON_WATER_FIX.isActive() && result.getReturnValue().consumesAction())
            user.awardStat(Stats.ITEM_USED.get(this));
    }
}
