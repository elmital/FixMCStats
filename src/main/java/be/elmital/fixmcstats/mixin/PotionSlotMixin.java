package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BrewingStandMenu.PotionSlot.class)
public class PotionSlotMixin extends Slot {
    // Fix https://bugs.mojang.com/browse/MC-154487
    public PotionSlotMixin(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
        if (Configs.CRAFTING_POTION_FIX.isActive())
            stack.onCraftedBy(player.level(), player, stack.getCount());
    }
}
