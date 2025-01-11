package be.elmital.fixmcstats.mixin.client;

import be.elmital.fixmcstats.Config;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screen.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StatsScreen.class)
public class StatsScreenMixin {

    // Fix https://bugs.mojang.com/browse/MC-36696
    @ModifyReturnValue(method = "shouldPause", at = @At(value = "RETURN"))
    public boolean shouldPauseOverride(boolean original) {
        if (!Config.instance().EXPERIMENTAL_STATS_SCREEN_TICK_FIX)
            return original;
        return true;
    }
}
