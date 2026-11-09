package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.interfaces.ItemRowWidgetClassIdentifier;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemDisplayWidget.class)
// Fix https://bugs.mojang.com/browse/MC-312667
public abstract class ItemDisplayWidgetMixin extends AbstractWidget {
    public ItemDisplayWidgetMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @ModifyExpressionValue(method = "extractWidgetRenderState", at = @At(target = "Lnet/minecraft/client/gui/components/ItemDisplayWidget;isHovered()Z", value = "INVOKE"))
    public boolean extractWidgetRenderState(boolean original) {
        if (!Configs.ITEM_TOOLTIP_KEYBOARD_FIX.isActive())
            return original;
        if ((Object) this instanceof ItemRowWidgetClassIdentifier) { // The ItemRowMixin implements this interface it allows us to check if it's this class and only this one is concerned (in case of other subclasses implementations) without instanceof check causing JVM access issues.
            return this.isHoveredOrFocused();
        }
        return original;
    }
}
