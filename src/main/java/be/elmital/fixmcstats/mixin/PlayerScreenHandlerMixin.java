package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerScreenHandler.class)
public class PlayerScreenHandlerMixin {
    // Fix https://bugs.mojang.com/browse/MC-157098
    @Inject(method = "quickMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;"))
    private void onInventoryFull(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 1) LocalRef<ItemStack> dropped) {
        dropped.get().onCraftByPlayer(player.getWorld(), player, dropped.get().getCount());
    }
}
