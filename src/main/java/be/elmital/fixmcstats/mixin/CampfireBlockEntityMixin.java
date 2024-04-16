package be.elmital.fixmcstats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(net.minecraft.block.entity.CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    // Fix https://bugs.mojang.com/browse/MC-144005
    @Inject(method = "litServerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V"))
    private static void ensureHolder(World world, BlockPos pos, BlockState state, net.minecraft.block.entity.CampfireBlockEntity campfire, CallbackInfo cir, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        NbtCompound nbt = itemStack.getNbt();
        UUID cooker = nbt != null ? nbt.getUuid("cooker") : null;
        if (cooker != null) {
            PlayerEntity playerEntity;
            if (world.getServer() != null) playerEntity = world.getServer().getPlayerManager().getPlayer(cooker);
            else playerEntity = world.getPlayerByUuid(cooker);

            if (playerEntity != null) itemStack2.onCraftByPlayer(world, playerEntity, 1);
        }
    }
}
