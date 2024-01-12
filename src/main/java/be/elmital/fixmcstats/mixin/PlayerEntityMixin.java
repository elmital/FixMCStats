package be.elmital.fixmcstats.mixin;

import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    // Fix https://bugs.mojang.com/browse/MC-211938
    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"))
    public void incrementStat(PlayerEntity instance, Identifier stat) {
        if (!(instance.getBlockStateAtPos().getBlock() instanceof ScaffoldingBlock))
            instance.incrementStat(stat);
    }
}
