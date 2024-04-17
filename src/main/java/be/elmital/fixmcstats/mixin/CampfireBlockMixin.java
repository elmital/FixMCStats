package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    // Fix https://bugs.mojang.com/browse/MC-144005
    @SuppressWarnings("deprecation")
    @Inject(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/CampfireBlockEntity;getRecipeFor(Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"))
    public void addCookerNBT(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir, @Local(index = 1, argsOnly = true) LocalRef<ItemStack> ref) {
        ItemStack stack2 = ref.get();
        stack2.getComponents().getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt().putUuid("cooker", player.getUuid());
        ref.set(stack2);
    }
}
