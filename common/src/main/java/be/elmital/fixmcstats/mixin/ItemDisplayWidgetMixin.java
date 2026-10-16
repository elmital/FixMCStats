package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemDisplayWidget.class)
public abstract class ItemDisplayWidgetMixin extends AbstractWidget {
    public ItemDisplayWidgetMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    // Fix https://bugs.mojang.com/browse/MC-312667
    @ModifyExpressionValue(method = "extractWidgetRenderState", at = @At(target = "Lnet/minecraft/client/gui/components/ItemDisplayWidget;isHovered()Z", value = "INVOKE"))
    public boolean extractWidgetRenderState(boolean original) {
        if (!Configs.ITEM_TOOLTIP_KEYBOARD_FIX.isActive())
            return original;
        if ((Object) this instanceof StatsScreen.ItemStatisticsList.ItemRow.ItemRowWidget)
            return this.isHoveredOrFocused();
        return original;
    }
}
