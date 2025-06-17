package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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


@SuppressWarnings("unused")
@Mixin(StonecutterScreenHandler.class)
public abstract class StonecutterScreenHandlerMixin extends ScreenHandler {
    @Invoker("populateResult")
    public abstract void invokePopulateResult(int selectedID);
    @Final @Shadow @Mutable
    Slot outputSlot;
    @Final @Shadow
    CraftingResultInventory output;
    @Final @Shadow
    Slot inputSlot;
    @Shadow
    long lastTakeTime;
    @Final @Shadow
    Property selectedRecipe;

    protected StonecutterScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    // Fix https://bugs.mojang.com/browse/MC-65198
    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void injected(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context, CallbackInfo ci) {
        if (!Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            return;

        int oldIndex = slots.indexOf(outputSlot);
        Slot newSlot = new Slot(output, 1, 143, 33) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            // Here is the fix for bug
            protected void onCrafted(ItemStack stack, int amount) {
                stack.onCraftByPlayer(playerInventory.player, amount);
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraftByPlayer(player, stack.getCount());
                output.unlockLastRecipe(player, this.getInputStacks());
                ItemStack itemStack = inputSlot.takeStack(1);
                if (!itemStack.isEmpty()) {
                    invokePopulateResult(selectedRecipe.get());
                }

                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (lastTakeTime != l) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        lastTakeTime = l;
                    }

                });
                super.onTakeItem(player, stack);
            }

            private List<ItemStack> getInputStacks() {
                return List.of(inputSlot.getStack());
            }
        };

        // Equivalent of calling ScreenHandler#addSlot()
        newSlot.id = outputSlot.id;
        slots.set(oldIndex, newSlot);

        // Remove the old one
        slots.remove(outputSlot);

        // Change the outputSLot
        outputSlot = newSlot;
    }
}