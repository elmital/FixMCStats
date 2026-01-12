package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    // Fix https://bugs.mojang.com/browse/MC-144005
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/CampfireBlockEntity;getCookableRecipe(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;"))
    public void addCookerNBT(ItemStack itemStack, net.minecraft.world.level.block.state.BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, net.minecraft.world.phys.BlockHitResult blockHitResult, CallbackInfoReturnable<ItemInteractionResult> cir, @Local(index = 1, argsOnly = true) LocalRef<ItemStack> ref) {
        ItemStack stack2 = ref.get();
        CustomData.update(DataComponents.CUSTOM_DATA, stack2, nbt -> nbt.putUUID("cooker", player.getUUID()));
        ref.set(stack2);
    }
}
