package be.elmital.fixmcstats.mixin;

import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Fix https://bugs.mojang.com/browse/MC-182814
@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {
    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"))
    private void cancelIncrement(ServerPlayerEntity instance, Stat<?> stat) {}

    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ConsumeItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;)V"))
    private void cancelCriteria(ConsumeItemCriterion instance, ServerPlayerEntity player, ItemStack stack) {}
}
