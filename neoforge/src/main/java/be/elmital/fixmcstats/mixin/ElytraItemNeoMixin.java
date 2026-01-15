package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraItem.class)
public class ElytraItemNeoMixin {
    // Fix https://bugs.mojang.com/browse/MC-122656
    @Inject(method = "elytraFlightTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = At.Shift.AFTER))
    public void incrementBreakingStat(ItemStack stack, LivingEntity entity, int flightTicks, CallbackInfoReturnable<Boolean> cir, @Local ItemStack elytra) {
        be.elmital.fixmcstats.mixinlogic.LivingEntity.checkAwarding(entity, elytra);
    }}

