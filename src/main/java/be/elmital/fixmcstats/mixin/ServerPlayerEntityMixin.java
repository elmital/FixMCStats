package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.StatisticUtils;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
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
    // Fix https://bugs.mojang.com/browse/MC/issues/MC-301722
    @Unique
    private @Nullable Vec3d lastVehiclePos = null;

    public ServerPlayerEntityMixin(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions) {
        super(world, profile);
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
    // Fix https://bugs.mojang.com/browse/MC/issues/MC-301722
    @Inject(method = "tickRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tickRiding()V"))
    private void modifyBeforeSuperTickRiding(CallbackInfo ci, @Local(ordinal = 0) LocalDoubleRef x, @Local(ordinal = 1) LocalDoubleRef y, @Local(ordinal = 2) LocalDoubleRef z) {
        if (getVehicle() != null) {
            if (lastVehiclePos == null)
                return;
            if ((Configs.CAMEL_STAT.isActive() && getVehicle() instanceof CamelEntity) || (Configs.HAPPY_GHAST_RIDING_FIX.isActive() && getVehicle() instanceof HappyGhastEntity)) {
                x.set(getVehicle().getX());
                y.set(getVehicle().getY());
                z.set(getVehicle().getZ());
            }
        }
    }

    // Fix https://bugs.mojang.com/browse/MC/issues/MC-256638 this part of the fix prevents stats incrementation when player turn while not moving
    // Fix https://bugs.mojang.com/browse/MC/issues/MC-301722
    @ModifyArgs(method = "tickRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;increaseRidingMotionStats(DDD)V"))
    private void modifyAfterSuperTickRiding(Args args) {
        if (getVehicle() != null) {
            if ((Configs.CAMEL_STAT.isActive() && getVehicle() instanceof CamelEntity) || (Configs.HAPPY_GHAST_RIDING_FIX.isActive() && getVehicle() instanceof HappyGhastEntity)) {
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

    // Fix https://bugs.mojang.com/browse/MC-211938
    @WrapWithCondition(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;incrementStat(Lnet/minecraft/util/Identifier;)V"))
    public boolean incrementStat(ServerPlayerEntity instance, Identifier identifier) {
        if (instance.getBlockStateAtPos().getBlock() instanceof ScaffoldingBlock)
            return !Configs.JUMP_WHEN_CLIMBING_SCAFFOLDING_FIX.isActive();
        return true;
    }
}
