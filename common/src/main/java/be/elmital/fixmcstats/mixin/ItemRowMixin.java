package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow
    @Final
    StatsScreen.ItemStatisticsList this$1;

    @Inject(method = "extractContent", at = @At("HEAD"))
    private void fixMCStats$captureMouse(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a, CallbackInfo ci) {
        // Capture the mouse position to render the tooltip in the above method
        this.fixMCStats$mouseX = mouseX;
        this.fixMCStats$mouseY = mouseY;
    }

    @WrapOperation(method = "extractStat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void fixMCStats$showFullValue(GuiGraphicsExtractor graphics, Font font, Component msg, int textX, int textY, int color, Operation<Void> original, @Local(argsOnly = true) Stat<?> stat) {
        if (stat == null || !Configs.ITEM_STATS_OVERLAPPING_FIX.isActive()) {
            original.call(graphics, font, msg, textX, textY, color);
            return;
        }

        StatsCounter stats = ((StatsScreenAccessor) (Object) ((ItemStatisticsListAccessor) this.this$1).getStatsScreen()).getStats();

        int value = stats.getValue(stat);
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
