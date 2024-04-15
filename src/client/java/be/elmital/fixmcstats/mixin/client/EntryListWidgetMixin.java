package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.ContainerWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@SuppressWarnings("unused")
@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin extends ContainerWidget {
    public EntryListWidgetMixin(int i, int j, int k, int l, Text text) {
        super(i, j, k, l, text);
    }

    // Fix https://bugs.mojang.com/browse/MC-189484
    @ModifyArgs(method = "renderEntry(Lnet/minecraft/client/gui/DrawContext;IIFIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;drawSelectionHighlight(Lnet/minecraft/client/gui/DrawContext;IIIII)V"))
    @SuppressWarnings("all")
    private void modifyYRenderEntry(Args args) {
        if (((ContainerWidget) this) instanceof StatsScreen.GeneralStatsListWidget) {
            args.set(1, ((int) args.get(1)) + 1);
            args.set(3, ((int) args.get(3)) + 1);
        } else if (((ContainerWidget) this) instanceof StatsScreen.ItemStatsListWidget) {
            args.set(1, ((int) args.get(1)) + 2);
        }
    }
}
