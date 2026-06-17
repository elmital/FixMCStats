package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandMenu.PotionSlot.class)
public class PotionSlotMixin {
    // Fix https://bugs.mojang.com/browse/MC-154487
    @Inject(method = "onTake", at = @At("TAIL"))
    public void onTake(Player player, ItemStack carried, CallbackInfo ci) {
        if (Configs.CRAFTING_POTION_FIX.isActive())
            carried.onCraftedBy(player, carried.getCount());
    }
}
