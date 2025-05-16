package be.elmital.fixmcstats.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

@Mixin(StatsScreen.GeneralStatisticsList.class)
public class GeneralStatsListWidgetMixin {

    // Fix https://bugs.mojang.com/browse/MC-178516
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;sort(Ljava/util/Comparator;)V", remap = false))
    private void sort(ObjectArrayList<Stat<ResourceLocation>> instance, Comparator<Stat<ResourceLocation>> comp, @Local(argsOnly = true) Minecraft ci) {
        Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setDecomposition(Collator.FULL_DECOMPOSITION);
        collator.setStrength(Collator.SECONDARY);
        instance.sort((e1, e2) -> collator.compare(I18n.get(StatsScreen.getTranslationKey(e1)), I18n.get(StatsScreen.getTranslationKey(e2))));
    }
}
