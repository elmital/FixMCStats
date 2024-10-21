package be.elmital.fixmcstats.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlaceableOnWaterItem.class)
public class PlaceableOnWaterItemMixin extends BlockItem {
    public PlaceableOnWaterItemMixin(Block block, net.minecraft.item.Item.Settings settings) {
        super(block, settings);
    }

    // TODO marked as in progress to check when release is out
    // Fix https://bugs.mojang.com/browse/MC-264274
    @Inject(method = "use", at = @At(value = "RETURN"))
    public void incrementStat(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> result) {
        if (result.getReturnValue().isAccepted())
            user.incrementStat(Stats.USED.getOrCreateStat(this));
    }
}
