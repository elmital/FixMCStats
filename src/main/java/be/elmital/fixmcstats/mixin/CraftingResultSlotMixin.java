package be.elmital.fixmcstats.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {
    // Fix https://bugs.mojang.com/browse/MC-65198 for drop
    @Redirect(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraftByPlayer(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V"))
    private void modifyIncrementedAmount(ItemStack stack, World world, PlayerEntity player, int amount) {
        stack.onCraftByPlayer(world, player, amount);
    }
}
