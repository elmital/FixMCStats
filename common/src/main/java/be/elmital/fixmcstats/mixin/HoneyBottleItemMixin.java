package be.elmital.fixmcstats.mixin;

import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Fix https://bugs.mojang.com/browse/MC-182814
@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {
    @Redirect(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/stats/Stat;)V"))
    private void cancelIncrement(ServerPlayer instance, Stat<?> stat) {}

    @Redirect(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ConsumeItemTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;)V"))
    private void cancelCriteria(ConsumeItemTrigger instance, ServerPlayer player, ItemStack stack) {}
}
