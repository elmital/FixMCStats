package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatisticsList.ItemRowComparator.class)
public class ItemComparatorMixin {
    // Fix https://bugs.mojang.com/browse/MC-213103
    @ModifyExpressionValue(method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;compare(II)I", ordinal = 0))
    public int compareEntries(int comparedInt, @Local(index = 3) Item item1, @Local(index = 4) Item item2) {
        if (Configs.ITEM_SORTING_FIX.isActive())
            return item1.getName(new ItemStack(item1)).getString().compareTo(item2.getName(new ItemStack(item1)).getString());
        return comparedInt;
    }
}
