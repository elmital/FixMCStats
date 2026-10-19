package be.elmital.fixmcstats.mixin;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.stats.StatsCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatsScreen.class)
public interface StatsScreenAccessor {
    // https://bugs.mojang.com/browse/169386
    @Accessor("stats")
    StatsCounter getStats();
}