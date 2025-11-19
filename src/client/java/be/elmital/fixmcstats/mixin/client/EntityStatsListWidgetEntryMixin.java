package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$MobsStatisticsList$MobRow")
@Environment(EnvType.CLIENT)
public class EntityStatsListWidgetEntryMixin {
    // Fix https://bugs.mojang.com/browse/MC-80827
    @ModifyVariable(method = "renderContent", at = @At(value = "INVOKE", target ="Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V", ordinal = 1, shift = At.Shift.AFTER), ordinal = 1, argsOnly = true)
    private int injected(int y) {
        if (Configs.MISSING_SPACE_STATS_SCREEN.isActive()) {
            return y + 1;
        }
        return y;
    }
}
