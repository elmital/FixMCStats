package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ResultSlot.class)
public class CraftingResultSlotMixin {
    // Fix https://bugs.mojang.com/browse/MC-65198 for drop
    @ModifyArg(method = "checkTakeAchievements", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V"))
    private int modifyIncrementedAmount(int amount, @Local(argsOnly = true) ItemStack stack) {
        if (Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            return stack.getCount();
        return amount;
    }
}
