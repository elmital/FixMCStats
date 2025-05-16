package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatisticsList.ItemRowComparator.class)
public class ItemComparatorMixin {
    // Fix https://bugs.mojang.com/browse/MC-213103
    @Redirect(method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;compare(II)I"))
    public int compareEntries(int item1, int item2) {
        return Item.byId(item1).getName().getString().compareTo(Item.byId(item2).getName().getString());
    }
}
