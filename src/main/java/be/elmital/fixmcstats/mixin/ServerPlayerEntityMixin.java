package be.elmital.fixmcstats.mixin;


import be.elmital.fixmcstats.Configs;
import be.elmital.fixmcstats.StatisticUtils;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("unused")
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
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
