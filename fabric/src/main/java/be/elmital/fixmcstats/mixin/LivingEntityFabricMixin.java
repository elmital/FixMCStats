package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityFabricMixin {
    // Fix https://bugs.mojang.com/browse/MC-122656
    @Inject(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = At.Shift.AFTER))
    public void incrementBreakingStat(CallbackInfo ci, @Local ItemStack elytra) {
        be.elmital.fixmcstats.mixinlogic.LivingEntity.checkAwarding(((LivingEntity) (Object) this), elytra);
    }
}
