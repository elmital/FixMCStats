package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

@Mixin(StatsScreen.ItemStatisticsList.ItemRow.class)
// https://bugs.mojang.com/browse/169386
public class ItemRowMixin {
    @Unique
    private final NumberFormat fixMCStats$compactFormatter = fixMCStats$generateFormatter();
    @Unique
    private int fixMCStats$mouseX;
    @Unique
    private int fixMCStats$mouseY;
    @Unique
    private int fixMCStats$statValue;

    @WrapOperation(method = "renderStat", at = @At(value = "INVOKE", target = "Lnet/minecraft/stats/StatsCounter;getValue(Lnet/minecraft/stats/Stat;)I"))
    private int fixMCStats$captureStatValue(StatsCounter stats, Stat<?> stat, Operation<Integer> original) {
        int value = original.call(stats, stat);
        this.fixMCStats$statValue = value;
        return value;
    }

    @Inject(method = "renderContent", at = @At("HEAD"))
    private void fixMCStats$captureMouse(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a, CallbackInfo ci) {
        // Capture the mouse position to render the tooltip in the fixMCStats$showFullValue method
        this.fixMCStats$mouseX = mouseX;
        this.fixMCStats$mouseY = mouseY;
    }

    @WrapOperation(method = "renderStat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void fixMCStats$showFullValue(GuiGraphics graphics, Font font, Component msg, int textX, int textY, int color, Operation<Void> original, @Local(argsOnly = true) Stat<?> stat) {
        if (stat == null || !Configs.ITEM_STATS_OVERLAPPING_FIX.isActive()) {
            original.call(graphics, font, msg, textX, textY, color);
            return;
        }

        int value = this.fixMCStats$statValue;
        if (value < 100000) {
            original.call(graphics, font, msg, textX, textY, color);
            return;
        }

        Component finalMsg = Component.literal(fixMCStats$compactFormatter.format(value));
        int textWidth = font.width(finalMsg);
        textX = textX + font.width(msg) - textWidth;
        original.call(graphics, font, finalMsg, textX, textY, color);

        if (this.fixMCStats$mouseX >= textX && this.fixMCStats$mouseX <= textX + textWidth && this.fixMCStats$mouseY >= textY && this.fixMCStats$mouseY < textY + 9) {
            graphics.setTooltipForNextFrame(
                    Component.literal(stat.format(value)),
                    this.fixMCStats$mouseX,
                    this.fixMCStats$mouseY
            );
        }
    }

    @Unique
    public NumberFormat fixMCStats$generateFormatter() {
        NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.getDefault(), NumberFormat.Style.SHORT);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter;
    }
}
