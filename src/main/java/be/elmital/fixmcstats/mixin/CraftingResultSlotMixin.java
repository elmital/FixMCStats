package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {
    // Fix https://bugs.mojang.com/browse/MC-65198 for drop
    @Redirect(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraftByPlayer(Lnet/minecraft/entity/player/PlayerEntity;I)V"))
    private void modifyIncrementedAmount(ItemStack stack, PlayerEntity player, int amount) {
        if (Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            stack.onCraftByPlayer(player, stack.getCount());
    }
}
