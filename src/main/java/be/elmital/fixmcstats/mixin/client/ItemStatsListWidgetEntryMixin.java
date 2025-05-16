package be.elmital.fixmcstats.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

@SuppressWarnings("unused")
@Mixin(StatsScreen.ItemStatisticsList.class)
public class ItemStatsListWidgetEntryMixin extends ObjectSelectionList<StatsScreen.ItemStatisticsList.ItemRow> {
    public ItemStatsListWidgetEntryMixin(Minecraft minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    // Fix https://bugs.mojang.com/browse/MC-139386
    @Redirect(method = "renderDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getName()Lnet/minecraft/network/chat/Component;"))
    private Component modifyItemName(Item item) {
        return Component.empty().append(item.getName()).withStyle(item.getDefaultInstance().getRarity().color());
    }

    // Fix https://bugs.mojang.com/browse/MC-213103
    @Inject(method = "<init>(Lnet/minecraft/client/gui/screens/achievement/StatsScreen;Lnet/minecraft/client/Minecraft;)V", at = @At(value = "TAIL"))
    public void onInit(StatsScreen statsScreen, Minecraft client, CallbackInfo info) {
        this.children().sort(Comparator.comparing(entry -> entry.getItem().getName().getString()));
    }
}
