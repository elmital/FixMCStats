package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.utils.StatisticUtils;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.mojang.authlib.GameProfile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
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
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {
    @Unique
    private @Nullable Vec3 lastVehiclePos = null;

    public ServerPlayerEntityMixin(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation) {
        super(serverLevel, serverLevel.getSharedSpawnPos(), serverLevel.getSharedSpawnAngle(), gameProfile);
    }

    // Fix for https://bugs.mojang.com/browse/MC-259687 - The stat is already incremented in the ServerPlayNetworkHandler
    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;checkMovementStatistics(DDD)V"), cancellable = true)
    private void cancelIfFallFlying(Vec3 movementInput, CallbackInfo ci) {
        if (Configs.ELYTRA_FIX.isActive() && this.isFallFlying() && !this.isSwimming() && !this.isEyeInFluid(FluidTags.WATER) && !this.isInWater() && !this.onClimbable() && !this.onGround())
            ci.cancel();
    }

    // Fix https://bugs.mojang.com/browse/MC-256638
    // Fix https://bugs.mojang.com/browse/MC-277294
    @ModifyArg(method = "checkRidingStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V", ordinal = 3), index = 0)
    private ResourceLocation modifyTargetStat(ResourceLocation identifier) {
        if (getVehicle() instanceof Camel && Configs.CAMEL_STAT.isActive())
            return StatisticUtils.CAMEL_RIDING_STAT.identifier();
        else if (getVehicle() instanceof Donkey && Configs.USE_DONKEY_STATS.isActive())
            return StatisticUtils.DONKEY_RIDING_STAT.identifier();
        else if (getVehicle() instanceof Mule && Configs.USE_MULE_STATS.isActive())
            return StatisticUtils.MULE_RIDING_STAT.identifier();
        else
            return identifier;
    }

    // Fix https://bugs.mojang.com/browse/MC/issues/MC-256638 this part of the fix prevents stats incrementation when player turn while not moving
    @Inject(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;checkRidingStatistics(DDD)V"))
    private void modifyBeforeSuperTickRiding(CallbackInfo ci, @Local(ordinal = 0) LocalDoubleRef x, @Local(ordinal = 1) LocalDoubleRef y, @Local(ordinal = 2) LocalDoubleRef z) {
        if (getVehicle() != null) {
            if (lastVehiclePos == null)
                return;
            if (Configs.CAMEL_STAT.isActive() && getVehicle() instanceof Camel) {
                x.set(getVehicle().getX());
                y.set(getVehicle().getY());
                z.set(getVehicle().getZ());
            }
        }
    }

    // Fix https://bugs.mojang.com/browse/MC/issues/MC-256638 this part of the fix prevents stats incrementation when player turn while not moving
    @ModifyArgs(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;checkRidingStatistics(DDD)V"))
    private void modifyAfterSuperTickRiding(Args args) {
        if (getVehicle() != null) {
            if (Configs.CAMEL_STAT.isActive() && getVehicle() instanceof Camel) {
                if (lastVehiclePos != null) {
                    // Remove the player location added earlier in tickRiding method in the locals because we don't use the player location we need the vehicle location instead
                    args.set(0, (double) args.get(0) - getX() + lastVehiclePos.x());
                    args.set(1, (double) args.get(1) - getY() + lastVehiclePos.y());
                    args.set(2, (double) args.get(2) - getZ() + lastVehiclePos.z());
                }
                lastVehiclePos = getVehicle().position();
            }
        } else {
            // Player dismount so clear the last vehicle position
            lastVehiclePos = null;
        }
    }

    // Fix for https://bugs.mojang.com/browse/MC-148457
    @ModifyArg(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V", ordinal = 6), index = 0)
    private ResourceLocation useCrawlStat(ResourceLocation identifier) {
        if (Configs.CRAWL_STAT.isActive() && this.isVisuallyCrawling())
            return StatisticUtils.CRAWL_ONE_CM.identifier();
        return identifier;
    }
}
