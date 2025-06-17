package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SkinOverlayOwner;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Fix https://bugs.mojang.com/browse/MC-147347
@SuppressWarnings("unused")
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements SkinOverlayOwner {
    @Unique
    private @Nullable PlayerEntity trackedIgniter;
    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;ignite()V"))
    private void trackIgniter(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (Configs.IGNITED_CREEPER_FIX.isActive())
            this.trackedIgniter = player;
    }

    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;shouldRenderOverlay()Z"))
    private void incrementDamageStat(CallbackInfo ci) {
        if (Configs.IGNITED_CREEPER_FIX.isActive() && this.trackedIgniter != null)
            this.trackedIgniter.incrementStat(Stats.KILLED.getOrCreateStat(this.getType()));
    }
}
