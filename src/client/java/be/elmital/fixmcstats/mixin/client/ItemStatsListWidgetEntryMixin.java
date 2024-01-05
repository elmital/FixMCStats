package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatsScreen.ItemStatsListWidget.class)
public class ItemStatsListWidgetEntryMixin {
    // Fix https://bugs.mojang.com/browse/MC-139386
    @Inject(method = "getText(Lnet/minecraft/item/Item;)Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
    private void modifyItemName(Item item, CallbackInfoReturnable<Text> text) {
        text.setReturnValue(Text.empty().append(item.getName()).formatted(item.getDefaultStack().getRarity().formatting));
    }
}
