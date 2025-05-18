package be.elmital.fixmcstats.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.world.inventory.BrewingStandMenu.PotionSlot")
public class PotionSlotMixin extends Slot {
    // Fix https://bugs.mojang.com/browse/MC-154487
    public PotionSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        stack.onCraftedBy(player, stack.getCount());
    }
}
