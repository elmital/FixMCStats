package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Fix https://bugs.mojang.com/browse/MC-147347
@SuppressWarnings("unused")
@Mixin(Creeper.class)
public abstract class CreeperEntityMixin extends Monster {
    @Unique
    private @Nullable Player trackedIgniter;
    protected CreeperEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Creeper;ignite()V"))
    private void trackIgniter(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (Configs.IGNITED_CREEPER_FIX.isActive())
            this.trackedIgniter = player;
    }
    @Inject(method = "explodeCreeper", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Creeper;triggerOnDeathMobEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity$RemovalReason;)V"))
    private void incrementDamageStat(CallbackInfo ci) {
        if (Configs.IGNITED_CREEPER_FIX.isActive() && this.trackedIgniter != null)
            this.trackedIgniter.awardStat(Stats.ENTITY_KILLED.get(this.getType()));
    }
}
