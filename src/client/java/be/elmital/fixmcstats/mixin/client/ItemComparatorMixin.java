package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatsListWidget.ItemComparator.class)
public class ItemComparatorMixin {
    // Fix https://bugs.mojang.com/browse/MC-213103
    @Redirect(method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$Entry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$Entry;)I", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;compare(II)I"))
    public int compareEntries(int item1, int item2) {
        return Item.byRawId(item1).getName().getString().compareTo(Item.byRawId(item2).getName().getString());
    }
}
