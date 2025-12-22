package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStand.class)
public class ArmorStandEntityMixin {

    // https://bugs.mojang.com/browse/MC-273933
    @Inject(method = "interactAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ArmorStand;swapItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Z"))
    public void equipEntity(Player player, Vec3 hitPos, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack stack) {
        if (Configs.EQUIP_ARMOR_STAND_FIX.isActive() && !stack.getItem().equals(Items.AIR)) {
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }
    }
}
