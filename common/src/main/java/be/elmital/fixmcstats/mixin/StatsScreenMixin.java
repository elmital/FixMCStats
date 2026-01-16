package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatsScreen.class)
public class StatsScreenMixin {
    // Fix https://bugs.mojang.com/browse/MC-213104
    @ModifyArg(method = "onStatsUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen;setActiveList(Lnet/minecraft/client/gui/components/ObjectSelectionList;)V"))
    public ObjectSelectionList<?> onStatsReady(@Nullable ObjectSelectionList<?> list) {
        var selected = ((StatsScreenAccessor) this).getActiveList();
        if (selected instanceof StatsScreen.GeneralStatisticsList) {
            return ((StatsScreenAccessor) this).getStatsList();
        } else if (selected instanceof StatsScreen.ItemStatisticsList) {
            return ((StatsScreenAccessor) this).getItemStatsList();
        } else if (selected instanceof StatsScreen.MobsStatisticsList) {
            return ((StatsScreenAccessor) this).getMobsStatsList();
        }
        return list;
    }

    // Fix https://bugs.mojang.com/browse/MC-36696
    @ModifyReturnValue(method = "isPauseScreen", at = @At(value = "RETURN"))
    public boolean shouldPauseOverride(boolean original) {
        if (!Configs.STATS_SCREEN_TICK_FIX.isActive())
            return original;
        return true;
    }
}
