package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Fix https://bugs.mojang.com/browse/MC-122656
    @Inject(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V", shift = At.Shift.AFTER))
    public void incrementBreakingStat(CallbackInfo ci, @Local ItemStack elytra) {
        if (!ElytraItem.isUsable(elytra)) {
            if (((LivingEntity) (Object) this) instanceof PlayerEntity playerEntity)
                playerEntity.incrementStat(Stats.BROKEN.getOrCreateStat(elytra.getItem()));
        }
    }
}
