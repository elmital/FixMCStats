package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    @Shadow @Final private DefaultedList<ItemStack> itemsBeingCooked;

    // Fix https://bugs.mojang.com/browse/MC-144005
    @Inject(method = "addItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    public void addCookerNBT(ServerWorld world, LivingEntity entity, ItemStack stack, CallbackInfoReturnable<Boolean> cir, @Local int index) {
        if (Configs.CAMP_FIRE_COOKING_CRAFT_STAT_FIX.isActive())
            NbtComponent.set(DataComponentTypes.CUSTOM_DATA, this.itemsBeingCooked.get(index), nbt -> nbt.put("cooker", Uuids.CODEC, entity.getUuid()));
    }

    // Fix https://bugs.mojang.com/browse/MC-144005
    @Inject(method = "litServerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V"))
    private static void ensureHolder(ServerWorld world, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity, ServerRecipeManager.MatchGetter<SingleStackRecipeInput, CampfireCookingRecipe> recipeMatchGetter, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        if (!Configs.CAMP_FIRE_COOKING_CRAFT_STAT_FIX.isActive())
            return;
        NbtComponent nbt = itemStack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if (nbt != null) {
            nbt.copyNbt().get("cooker", Uuids.CODEC).ifPresent(cooker -> {
                PlayerEntity playerEntity = world.getServer().getPlayerManager().getPlayer(cooker);

                if (playerEntity != null) itemStack2.onCraftByPlayer(playerEntity, 1);
            });
        }
    }
}
