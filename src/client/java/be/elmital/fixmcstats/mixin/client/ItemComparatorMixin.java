package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatsListWidget.ItemComparator.class)
public class ItemComparatorMixin {
    // Fix https://bugs.mojang.com/browse/MC-213103
    @ModifyExpressionValue(method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;compare(II)I", ordinal = 0))
    public int compareEntries(int comparedInt, @Local(index = 3) Item item1, @Local(index = 4) Item item2) {
        if (Configs.ITEM_SORTING_FIX.isActive())
            return item1.getName().getString().compareTo(item2.getName().getString());
        return comparedInt;
    }
}
