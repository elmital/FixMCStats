package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    // Fix https://bugs.mojang.com/browse/MC-144005
    @Inject(method = "cookTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Containers;dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"))
    private static void ensureHolder(Level world, BlockPos blockPos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        if (!Configs.CAMP_FIRE_COOKING_CRAFT_STAT_FIX.isActive())
            return;
        CustomData nbt = itemStack.getComponents().get(DataComponents.CUSTOM_DATA);
        UUID cooker = nbt != null ? nbt.copyTag().getUUID("cooker") : null;
        if (cooker != null) {
            Player playerEntity;
            if (world.getServer() != null) playerEntity = world.getServer().getPlayerList().getPlayer(cooker);
            else playerEntity = world.getPlayerByUUID(cooker);

            if (playerEntity != null) itemStack2.onCraftedBy(world, playerEntity, 1);
        }
    }
}
