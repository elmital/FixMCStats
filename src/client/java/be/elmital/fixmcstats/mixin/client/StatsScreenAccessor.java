package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatsScreen.class)
public interface StatsScreenAccessor {
    @Accessor
    AlwaysSelectedEntryListWidget<?> getSelectedList(); // Fix https://bugs.mojang.com/browse/MC-213104
    @Accessor
    StatsScreen.GeneralStatsListWidget getGeneralStats(); // Fix https://bugs.mojang.com/browse/MC-213104
    @Accessor
    StatsScreen.ItemStatsListWidget getItemStats(); // Fix https://bugs.mojang.com/browse/MC-213104
    @Accessor
    StatsScreen.EntityStatsListWidget getMobStats(); // Fix https://bugs.mojang.com/browse/MC-213104
}
