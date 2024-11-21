package be.elmital.fixmcstats.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

@Mixin(StatsScreen.GeneralStatsListWidget.class)
public class GeneralStatsListWidgetMixin {

    // Fix https://bugs.mojang.com/browse/MC-178516
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;sort(Ljava/util/Comparator;)V", remap = false))
    private void sort(ObjectArrayList<Stat<net.minecraft.util.Identifier>> instance, Comparator<Stat<net.minecraft.util.Identifier>> comp, @Local(argsOnly = true) MinecraftClient ci) {
        Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setDecomposition(Collator.FULL_DECOMPOSITION);
        collator.setStrength(Collator.SECONDARY);
        instance.sort((e1,e2) -> collator.compare(I18n.translate(StatsScreen.getStatTranslationKey(e1)), I18n.translate(StatsScreen.getStatTranslationKey(e2))));
    }
}
