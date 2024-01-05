package be.elmital.fixmcstats.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatsScreen.class)
@Environment(EnvType.CLIENT)
public class StatsScreenMixin {
    // Fix https://bugs.mojang.com/browse/MC-213104
    @ModifyArg(method = "onStatsReady()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/StatsScreen;selectStatList(Lnet/minecraft/client/gui/widget/AlwaysSelectedEntryListWidget;)V"))
    public AlwaysSelectedEntryListWidget<?> onStatsReady(@Nullable AlwaysSelectedEntryListWidget<?> list) {
        var selected = ((StatsScreenAccessor) this).getSelectedList();
        if (selected instanceof StatsScreen.GeneralStatsListWidget) {
            return ((StatsScreenAccessor) this).getGeneralStats();
        } else if (selected instanceof StatsScreen.ItemStatsListWidget) {
            return ((StatsScreenAccessor) this).getItemStats();
        } else if (selected instanceof StatsScreen.EntityStatsListWidget) {
            return ((StatsScreenAccessor) this).getMobStats();
        }
        return list;
    }
}
