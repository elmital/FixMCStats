package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxPlayable.class)
public class JukeboxPlayableMixin {

    // https://bugs.mojang.com/browse/MC-276994
    @Inject(method = "tryInsertIntoJukebox", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;setTheItem(Lnet/minecraft/world/item/ItemStack;)V"))
    private static void awardRecordUseStat(Level level, BlockPos pos, ItemStack stack, Player player, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 1) ItemStack itemStack) {
        if (Configs.INCREMENT_USE_RECORDS.isActive())
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
    }
}
