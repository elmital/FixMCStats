package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.gui.screen.StatsScreen$EntityStatsListWidget$Entry")
@Environment(EnvType.CLIENT)
public class EntityStatsListWidgetEntryMixin {
    // Fix https://bugs.mojang.com/browse/MC-80827
    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/DrawContext;IIIIIIIZF)V", at = @At(value = "INVOKE", target ="Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V" , shift = At.Shift.AFTER), ordinal = 1, argsOnly = true)
    private int injected(int y) {
        if (Configs.MISSING_SPACE_STATS_SCREEN.isActive())
            return y + 1;
        return y;
    }
}
