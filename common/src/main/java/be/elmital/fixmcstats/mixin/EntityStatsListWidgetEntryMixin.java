package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$MobsStatisticsList$MobRow")
public class EntityStatsListWidgetEntryMixin {
    // Fix https://bugs.mojang.com/browse/MC-80827
    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I", shift = At.Shift.AFTER), ordinal = 1, argsOnly = true)
    private int injected(int y) {
        if (Configs.MISSING_SPACE_STATS_SCREEN.isActive())
            return y + 1;
        return y;
    }
}
