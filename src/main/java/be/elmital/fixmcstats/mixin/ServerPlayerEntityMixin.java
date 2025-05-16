package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Config;
import be.elmital.fixmcstats.StatisticUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ScaffoldingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {
    public ServerPlayerEntityMixin(ServerLevel world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    // Fix https://bugs.mojang.com/browse/MC-256638
    @ModifyArg(method = "checkRidingStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V", ordinal = 3), index = 0)
    private ResourceLocation modifyTargetStat(ResourceLocation identifier) {
        return getVehicle() instanceof Camel && Config.instance().USE_CAMEL_CUSTOM_STAT ? StatisticUtils.CAMEL_RIDING_STAT.identifier() : identifier;
    }

    // Fix for https://bugs.mojang.com/browse/MC-148457
    @ModifyArg(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V", ordinal = 6), index = 0)
    private ResourceLocation useCrawlStat(ResourceLocation identifier) {
        if (Config.instance().USE_CRAWL_CUSTOM_STAT && this.isVisuallyCrawling())
            return StatisticUtils.CRAWL_ONE_CM.identifier();
        return identifier;
    }

    // Fix https://bugs.mojang.com/browse/MC-211938
    @Redirect(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;)V"))
    public void incrementStat(ServerPlayer instance, ResourceLocation identifier) {
        if (!(instance.getInBlockState().getBlock() instanceof ScaffoldingBlock))
            instance.awardStat(identifier);
    }
}
