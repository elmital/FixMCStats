package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Config;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    // Experimental fix for https://bugs.mojang.com/browse/MC-259687
    @ModifyArg(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 7), index = 1)
    private int modifyDistance(int value) {
        if (Config.instance().ELYTRA_EXPERIMENTAL_FIX)
            return value / 2;
        return value;
    }
}
