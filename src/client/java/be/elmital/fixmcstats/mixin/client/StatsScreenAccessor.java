package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatsScreen.class)
public interface StatsScreenAccessor {
    @Accessor
    ObjectSelectionList<?> getActiveList(); // Fix https://bugs.mojang.com/browse/MC-213104
    @Accessor
    StatsScreen.GeneralStatisticsList getStatsList(); // Fix https://bugs.mojang.com/browse/MC-213104
    @Accessor
    StatsScreen.ItemStatisticsList getItemStatsList(); // Fix https://bugs.mojang.com/browse/MC-213104
    @Accessor
    StatsScreen.MobsStatisticsList getMobsStatsList(); // Fix https://bugs.mojang.com/browse/MC-213104
}
