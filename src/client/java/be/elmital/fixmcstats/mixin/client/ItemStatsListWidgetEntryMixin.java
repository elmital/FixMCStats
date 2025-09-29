package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.LanguageBasedCollator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.text.Collator;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatsListWidget.class)
public abstract class ItemStatsListWidgetEntryMixin extends ElementListWidget<StatsScreen.ItemStatsListWidget.Entry> {
    public ItemStatsListWidgetEntryMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    // Fix https://bugs.mojang.com/browse/MC-213103
    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/StatsScreen;Lnet/minecraft/client/MinecraftClient;)V", at = @At(value = "TAIL"))
    public void onInit(StatsScreen statsScreen, MinecraftClient client, CallbackInfo info) {
        if (Configs.ITEM_SORTING_FIX.isActive()) {
            final Collator collator = LanguageBasedCollator.generateCollator();
            sort((o1, o2) -> {
                if (o1 instanceof StatsScreen.ItemStatsListWidget.StatEntry statEntry1 && o2 instanceof StatsScreen.ItemStatsListWidget.StatEntry statEntry2) {
                    return collator.compare(statEntry1.getItem().getName().getString(), statEntry2.getItem().getName().getString());
                }
                return 0;
            });
        }
    }
}
