package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.LanguageBasedCollator;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.text.Collator;
import java.util.Comparator;

@Mixin(StatsScreen.GeneralStatisticsList.class)
public class GeneralStatsListWidgetMixin {
    @Unique
    private final static Collator fixCollator = LanguageBasedCollator.generateCollator();

    // Fix https://bugs.mojang.com/browse/MC-178516
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;sort(Ljava/util/Comparator;)V", remap = false))
    private Comparator<Stat<ResourceLocation>> sort(Comparator<Stat<ResourceLocation>> comp) {
        if (Configs.SORTED_STATS_SCREEN.isActive())
            return (e1,e2) -> fixCollator.compare(I18n.get(StatsScreen.getTranslationKey(e1)), I18n.get(StatsScreen.getTranslationKey(e2)));
        else
            return comp;
    }
}
