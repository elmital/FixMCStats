package be.elmital.fixmcstats.mixin;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatsScreen.ItemStatisticsList.class)
public interface ItemStatisticsListAccessor {
    // https://bugs.mojang.com/browse/169386
    @Accessor("this$0")
    StatsScreen getStatsScreen();
}
