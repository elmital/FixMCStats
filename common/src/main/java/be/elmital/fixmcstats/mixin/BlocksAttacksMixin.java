package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.BlocksAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlocksAttacks.class)
public class BlocksAttacksMixin {

    // https://bugs.mojang.com/browse/MC-298805
    @Redirect(method = "hurtBlockingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    private void cancelStatAward(Player instance, Stat<?> stat, @Local(argsOnly = true) float damages) {
        if (!Configs.SHIELD_USED_FIX.isActive() || damages > 0.0F) {
            instance.awardStat(stat);
        }
    }
}
