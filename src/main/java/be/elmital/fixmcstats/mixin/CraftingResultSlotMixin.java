package be.elmital.fixmcstats.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ResultSlot.class)
public class CraftingResultSlotMixin {
    // Fix https://bugs.mojang.com/browse/MC-65198 for drop
    @Redirect(method = "checkTakeAchievements(Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy(Lnet/minecraft/world/entity/player/Player;I)V"))
    private void modifyIncrementedAmount(ItemStack stack, Player player, int amount) {
        stack.onCraftedBy(player, stack.getCount());
    }
}
