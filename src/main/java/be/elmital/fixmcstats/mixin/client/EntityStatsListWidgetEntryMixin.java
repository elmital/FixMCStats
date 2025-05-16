package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StatsScreen.MobsStatisticsList.MobRow.class)
@OnlyIn(Dist.CLIENT)
public class EntityStatsListWidgetEntryMixin {
    // Fix https://bugs.mojang.com/browse/MC-80827
    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I", shift = At.Shift.AFTER), ordinal = 1, argsOnly = true)
    private int injected(int y) {
        return y + 1;
    }
}
