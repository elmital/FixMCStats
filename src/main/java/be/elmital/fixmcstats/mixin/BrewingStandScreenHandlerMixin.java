package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BrewingStandScreenHandler.class)
public abstract class BrewingStandScreenHandlerMixin {
    // FIX https://bugs.mojang.com/browse/MC-154487
    @Inject(method = "quickMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;onQuickTransfer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    protected void incrementCraftStat(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 0) LocalRef<ItemStack> item) {
        item.get().onCraftByPlayer(player.getWorld(), player, item.get().getCount());
    }

}
