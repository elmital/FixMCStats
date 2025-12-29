package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.BlocksAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlocksAttacks.class)
public class BlocksAttacksNeoMixin {

    // https://bugs.mojang.com/browse/MC-298805
    @Redirect(method = "Lnet/minecraft/world/item/component/BlocksAttacks;hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    private void awardingStat(Player instance, Stat<?> stat, @Local(argsOnly = true) float damages) {
        be.elmital.fixmcstats.mixinlogic.BlocksAttacks.checkAwardingStat(instance, stat, damages);
    }
}
