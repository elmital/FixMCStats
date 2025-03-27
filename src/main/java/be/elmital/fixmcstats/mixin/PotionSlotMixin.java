package be.elmital.fixmcstats.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BrewingStandScreenHandler.PotionSlot.class)
public class PotionSlotMixin extends Slot {
    // Fix https://bugs.mojang.com/browse/MC-154487
    public PotionSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
        stack.onCraftByPlayer(player, stack.getCount());
    }
}
