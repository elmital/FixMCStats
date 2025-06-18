package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {
    // Fix https://bugs.mojang.com/browse/MC-65198 for drop
    @ModifyArg(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraftByPlayer(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V"))
    private int modifyIncrementedAmount(int amount, @Local(argsOnly = true) ItemStack stack) {
        if (Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            return stack.getCount();
        return amount;
    }
}
