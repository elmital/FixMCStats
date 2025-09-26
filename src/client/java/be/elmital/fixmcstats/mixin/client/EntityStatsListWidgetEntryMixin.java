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
    // TODO MAY BE FIXED CHECK MOJIRA WHEN THE SITE ISN'T BROKEN...
    // Fix https://bugs.mojang.com/browse/MC-80827
    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target ="Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 1, shift = At.Shift.AFTER), ordinal = 1, argsOnly = true)
    private int injected(int y) {
        if (Configs.MISSING_SPACE_STATS_SCREEN.isActive()) {
            return y + 1;
        }
        return y;
    }
}
