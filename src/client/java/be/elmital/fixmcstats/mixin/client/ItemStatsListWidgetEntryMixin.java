package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.Comparator;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatsListWidget.class)
public class ItemStatsListWidgetEntryMixin extends AlwaysSelectedEntryListWidget<StatsScreen.ItemStatsListWidget.Entry> {
    public ItemStatsListWidgetEntryMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    // Fix https://bugs.mojang.com/browse/MC-139386
    @Redirect(method = "renderDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getName()Lnet/minecraft/text/Text;"))
    private Text modifyItemName(Item item) {
        return Text.empty().append(item.getName()).formatted(item.getDefaultStack().getRarity().getFormatting());
    }

    // Fix https://bugs.mojang.com/browse/MC-213103
    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/StatsScreen;Lnet/minecraft/client/MinecraftClient;)V", at = @At(value = "TAIL"))
    public void onInit(StatsScreen statsScreen, MinecraftClient client, CallbackInfo info) {
        this.children().sort(Comparator.comparing(entry -> entry.getItem().getName().getString()));
    }
}
