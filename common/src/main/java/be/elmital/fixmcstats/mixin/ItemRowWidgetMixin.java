package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.interfaces.ItemRowWidgetClassIdentifier;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;

// Fix https://bugs.mojang.com/browse/MC-312667 for this we only implement the ItemRowWidgetClassIdentifier class that allows the patch in the ItemDisplayWidgetMixin to work correctly
@Mixin(StatsScreen.ItemStatisticsList.ItemRow.ItemRowWidget.class)
public abstract class ItemRowWidgetMixin implements ItemRowWidgetClassIdentifier {
}
