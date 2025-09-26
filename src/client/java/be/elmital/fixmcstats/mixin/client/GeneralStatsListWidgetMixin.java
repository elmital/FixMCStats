package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.LanguageBasedCollator;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.text.Collator;
import java.util.Comparator;

@Mixin(StatsScreen.GeneralStatsListWidget.class)
public class GeneralStatsListWidgetMixin {
    @Unique
    private final static Collator fixCollator = LanguageBasedCollator.generateCollator();

    // Fix https://bugs.mojang.com/browse/MC-178516
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;sort(Ljava/util/Comparator;)V", remap = false))
    private Comparator<Stat<Identifier>> sort(Comparator<Stat<Identifier>> comp) {
        if (Configs.SORTED_STATS_SCREEN.isActive())
            return (e1,e2) -> fixCollator.compare(I18n.translate(StatsScreen.getStatTranslationKey(e1)), I18n.translate(StatsScreen.getStatTranslationKey(e2)));
        else
            return comp;
    }
}
