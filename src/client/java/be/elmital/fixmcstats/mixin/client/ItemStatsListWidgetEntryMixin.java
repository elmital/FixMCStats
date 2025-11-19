package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.LanguageBasedCollator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.text.Collator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatisticsList.class)
public abstract class ItemStatsListWidgetEntryMixin extends ContainerObjectSelectionList<StatsScreen.ItemStatisticsList.Entry> {
    public ItemStatsListWidgetEntryMixin(Minecraft minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    // Fix https://bugs.mojang.com/browse/MC-213103
    @Inject(method = "<init>(Lnet/minecraft/client/gui/screens/achievement/StatsScreen;Lnet/minecraft/client/Minecraft;)V", at = @At(value = "TAIL"))
    public void onInit(StatsScreen statsScreen, Minecraft client, CallbackInfo info) {
        if (Configs.ITEM_SORTING_FIX.isActive()) {
            final Collator collator = LanguageBasedCollator.generateCollator();
            sort((o1, o2) -> {
                if (o1 instanceof StatsScreen.ItemStatisticsList.ItemRow statEntry1 && o2 instanceof StatsScreen.ItemStatisticsList.ItemRow statEntry2) {
                    return collator.compare(statEntry1.getItem().getName().getString(), statEntry2.getItem().getName().getString());
                }
                return 0;
            });
        }
    }
}
