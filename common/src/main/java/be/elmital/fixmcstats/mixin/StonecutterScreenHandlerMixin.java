package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;


@SuppressWarnings("unused")
@Mixin(StonecutterMenu.class)
public abstract class StonecutterScreenHandlerMixin extends AbstractContainerMenu {
    @Invoker("setupResultSlot")
    public abstract void invokePopulateResult(int selectedID);
    @Final @Shadow @Mutable
    Slot resultSlot;
    @Final @Shadow
    ResultContainer resultContainer;
    @Final @Shadow
    Slot inputSlot;
    @Shadow
    long lastSoundTime;
    @Final @Shadow
    DataSlot selectedRecipeIndex;

    protected StonecutterScreenHandlerMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    // Fix https://bugs.mojang.com/browse/MC-65198
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void injected(int syncId, Inventory playerInventory, final ContainerLevelAccess context, CallbackInfo ci) {
        if (!Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            return;

        int oldIndex = slots.indexOf(resultSlot);
        Slot newSlot = new Slot(resultContainer, 1, 143, 33) {
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            // Here is the fix for bug
            protected void onQuickCraft(ItemStack stack, int amount) {
                stack.onCraftedBy(playerInventory.player, amount);
            }

            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player, stack.getCount());
                resultContainer.awardUsedRecipes(player, this.getInputStacks());
                ItemStack itemStack = inputSlot.remove(1);
                if (!itemStack.isEmpty()) {
                    invokePopulateResult(selectedRecipeIndex.get());
                }

                context.execute((world, pos) -> {
                    long l = world.getGameTime();
                    if (lastSoundTime != l) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        lastSoundTime = l;
                    }

                });
                super.onTake(player, stack);
            }

            private List<ItemStack> getInputStacks() {
                return List.of(inputSlot.getItem());
            }
        };

        // Equivalent of calling ScreenHandler#addSlot()
        newSlot.index = resultSlot.index;
        slots.set(oldIndex, newSlot);

        // Remove the old one
        slots.remove(resultSlot);

        // Change the outputSLot
        resultSlot = newSlot;
    }
}