package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.LanguageBasedCollator;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.text.Collator;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatisticsList.class)
public abstract class ItemStatsListWidgetEntryMixin extends ObjectSelectionList<StatsScreen.ItemStatisticsList.ItemRow> {
    public ItemStatsListWidgetEntryMixin(Minecraft minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    // Fix https://bugs.mojang.com/browse/MC-139386
    @ModifyArg(method = "renderDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;II)V", ordinal = 0))
    private Component modifyItemName(Component text, @Local Item item) {
        if (Configs.RARE_BLOCKS_COLORS.isActive())
            return Component.empty().append(item.getName(item.getDefaultInstance())).withStyle(item.getDefaultInstance().getRarity().color());
        return text;
    }

    // Fix https://bugs.mojang.com/browse/MC-213103
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void onInit(StatsScreen statsScreen, Minecraft minecraft, CallbackInfo ci) {
        if (Configs.ITEM_SORTING_FIX.isActive()) {
            final Collator collator = LanguageBasedCollator.generateCollator();
            this.children().sort((o1, o2) -> {
                if (o1 instanceof StatsScreen.ItemStatisticsList.ItemRow statEntry1 && o2 instanceof StatsScreen.ItemStatisticsList.ItemRow statEntry2) {
                    return collator.compare(statEntry1.getItem().getName(statEntry1.getItem().getDefaultInstance()).getString(), statEntry2.getItem().getName(statEntry2.getItem().getDefaultInstance()).getString());
                }
                return 0;
            });
        }
    }
}
