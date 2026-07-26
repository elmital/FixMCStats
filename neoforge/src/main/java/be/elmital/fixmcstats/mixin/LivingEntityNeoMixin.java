package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityNeoMixin {
    // Fix https://bugs.mojang.com/browse/MC-122656
    @WrapOperation(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onGlideDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
    public void incrementBreakingStat(ItemStack instance, LivingEntity livingEntity, EquipmentSlot equipmentSlot, Operation<Void> original) {
        original.call(instance, livingEntity, equipmentSlot);
        be.elmital.fixmcstats.mixinlogic.LivingEntity.awardElytraBrokenStat(instance, (LivingEntity) (Object) this);
    }
}
