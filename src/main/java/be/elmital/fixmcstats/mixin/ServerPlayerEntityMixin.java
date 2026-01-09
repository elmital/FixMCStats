package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.StatisticUtils;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@SuppressWarnings("unused")
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Unique
    private @Nullable Vec3d lastVehiclePos = null;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    // Fix for https://bugs.mojang.com/browse/MC-259687 - The stat is already incremented in the ServerPlayNetworkHandler
    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseTravelMotionStats(DDD)V"), cancellable = true)
    private void cancelIfFallFlying(Vec3d movementInput, CallbackInfo ci) {
        if (Configs.ELYTRA_FIX.isActive() && this.isFallFlying() && !this.isSwimming() && !this.isSubmergedIn(FluidTags.WATER) && !this.isTouchingWater() && !this.isClimbing() && !this.isOnGround())
            ci.cancel();
    }

    // Fix https://bugs.mojang.com/browse/MC-256638
    // Fix https://bugs.mojang.com/browse/MC-277294
    @ModifyArg(method = "increaseRidingMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 3), index = 0)
    private Identifier modifyTargetStat(Identifier identifier) {
        if (getVehicle() instanceof CamelEntity && Configs.CAMEL_STAT.isActive())
            return StatisticUtils.CAMEL_RIDING_STAT.identifier();
        else if (getVehicle() instanceof DonkeyEntity && Configs.USE_DONKEY_STATS.isActive())
            return StatisticUtils.DONKEY_RIDING_STAT.identifier();
        else if (getVehicle() instanceof MuleEntity && Configs.USE_MULE_STATS.isActive())
            return StatisticUtils.MULE_RIDING_STAT.identifier();
        else
            return identifier;
    }

    // Fix https://bugs.mojang.com/browse/MC/issues/MC-256638 this part of the fix prevents stats incrementation when player turn while not moving
    @Inject(method = "tickRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tickRiding()V"))
    private void modifyBeforeSuperTickRiding(CallbackInfo ci, @Local(ordinal = 0) LocalDoubleRef x, @Local(ordinal = 1) LocalDoubleRef y, @Local(ordinal = 2) LocalDoubleRef z) {
        if (getVehicle() != null) {
            if (lastVehiclePos == null)
                return;
            if (Configs.CAMEL_STAT.isActive() && getVehicle() instanceof CamelEntity) {
                x.set(getVehicle().getX());
                y.set(getVehicle().getY());
                z.set(getVehicle().getZ());
            }
        }
    }

    // Fix https://bugs.mojang.com/browse/MC/issues/MC-256638 this part of the fix prevents stats incrementation when player turn while not moving
    @ModifyArgs(method = "tickRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseRidingMotionStats(DDD)V"))
    private void modifyAfterSuperTickRiding(Args args) {
        if (getVehicle() != null) {
            if (Configs.CAMEL_STAT.isActive() && getVehicle() instanceof CamelEntity) {
                if (lastVehiclePos != null) {
                    // Remove the player location added earlier in tickRiding method in the locals because we don't use the player location we need the vehicle location instead
                    args.set(0, (double) args.get(0) - getX() + lastVehiclePos.getX());
                    args.set(1, (double) args.get(1) - getY() + lastVehiclePos.getY());
                    args.set(2, (double) args.get(2) - getZ() + lastVehiclePos.getZ());
                }
                lastVehiclePos = getVehicle().getPos();
            }
        } else {
            // Player dismount so clear the last vehicle position
            lastVehiclePos = null;
        }
    }

    // Fix for https://bugs.mojang.com/browse/MC-148457
    @ModifyArg(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseStat(Lnet/minecraft/util/Identifier;I)V", ordinal = 6), index = 0)
    private Identifier useCrawlStat(Identifier identifier) {
        if (Configs.CRAWL_STAT.isActive() && this.isCrawling())
            return StatisticUtils.CRAWL_ONE_CM.identifier();
        return identifier;
    }
}
