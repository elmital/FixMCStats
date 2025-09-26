package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.LanguageBasedCollator;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.text.Collator;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatsListWidget.class)
public abstract class ItemStatsListWidgetEntryMixin extends AlwaysSelectedEntryListWidget<StatsScreen.ItemStatsListWidget.Entry> {
    public ItemStatsListWidgetEntryMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    // Fix https://bugs.mojang.com/browse/MC-139386
    @ModifyArg(method = "renderDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;II)V", ordinal = 0))
    private Text modifyItemName(Text text, @Local Item item) {
        if (Configs.RARE_BLOCKS_COLORS.isActive())
            return Text.empty().append(item.getName()).formatted(item.getDefaultStack().getRarity().getFormatting());
        return text;
    }

    // Fix https://bugs.mojang.com/browse/MC-213103
    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/StatsScreen;Lnet/minecraft/client/MinecraftClient;)V", at = @At(value = "TAIL"))
    public void onInit(StatsScreen statsScreen, MinecraftClient client, CallbackInfo info) {
        if (Configs.ITEM_SORTING_FIX.isActive()) {
            final Collator collator = LanguageBasedCollator.generateCollator();
            this.children().sort((o1, o2) -> {
                if (o1 instanceof StatsScreen.ItemStatsListWidget.Entry statEntry1 && o2 instanceof StatsScreen.ItemStatsListWidget.Entry statEntry2) {
                    return collator.compare(statEntry1.getItem().getName().getString(), statEntry2.getItem().getName().getString());
                }
                return 0;
            });
        }
    }
}
