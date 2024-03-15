package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Config;
import be.elmital.fixmcstats.StatisticUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    // Fix for https://bugs.mojang.com/browse/MC-259687 - The stat is already incremented in the ServerPlayNetworkHandler
    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseTravelMotionStats(DDD)V"), cancellable = true)
    private void cancelIfFallFlying(Vec3d movementInput, CallbackInfo ci) {
        if (this.isFallFlying() && !this.isSwimming() && !this.isSubmergedIn(FluidTags.WATER) && !this.isTouchingWater() && !this.isClimbing() && !this.isOnGround())
            ci.cancel();
    }

    // Fix https://bugs.mojang.com/browse/MC-256638
    @ModifyArg(method = "increaseRidingMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 3), index = 0)
    private Identifier modifyTargetStat(Identifier identifier) {
        return getVehicle() instanceof CamelEntity && Config.instance().USE_CAMEL_CUSTOM_STAT ? StatisticUtils.CAMEL_RIDING_STAT.identifier() : identifier;
    }

    // Fix for https://bugs.mojang.com/browse/MC-148457
    @ModifyArg(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 6), index = 0)
    private Identifier useCrawlStat(Identifier identifier) {
        if (Config.instance().USE_CRAWL_CUSTOM_STAT && this.isCrawling())
            return StatisticUtils.CRAWL_ONE_CM.identifier();
        return identifier;
    }
}
