package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Fix https://bugs.mojang.com/browse/MC-254512 && https://bugs.mojang.com/browse/MC-214457
@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(method = "shootAll", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V"), cancellable = true)
    private void cancelTardiveMethodCall(CallbackInfo info) {
        if (Configs.BREAKING_CROSSBOW_FIX.isActive())
            info.cancel();
    }

    @Inject(method = "shootAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V"))
    private void awardPlayer(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, LivingEntity livingEntity, CallbackInfo ci) {
        if (Configs.BREAKING_CROSSBOW_FIX.isActive() && shooter instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }
    }
}
