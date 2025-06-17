package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("unused")
@Mixin(ForgingScreenHandler.class)
public abstract class ForgingScreenHandlerMixin extends ScreenHandler {
    @Final
    @Shadow
    protected PlayerEntity player;
    @Invoker("canTakeOutput")
    public abstract boolean invokeCanTakeOutput(PlayerEntity player, boolean present);
    @Invoker("onTakeOutput")
    public abstract void invokeOnTakeOutput(PlayerEntity player, ItemStack stack);

    @Final
    @Shadow
    protected CraftingResultInventory output;
    protected ForgingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    // Fix https://bugs.mojang.com/browse/MC-65198 for Smithing table
    @ModifyArg(method = "addResultSlot(Lnet/minecraft/screen/slot/ForgingSlotsManager;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ForgingScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"))
    private Slot modifySlot(Slot slot, @Local(argsOnly = true) ForgingSlotsManager manager) {
        if (!Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            return slot;
        return new Slot(this.output, manager.getResultSlot().slotId(), manager.getResultSlot().x(), manager.getResultSlot().y()) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public void onCrafted(ItemStack stack, int amount) {
                if (((ForgingScreenHandler) (Object) ForgingScreenHandlerMixin.this) instanceof SmithingScreenHandler)
                    stack.onCraftByPlayer(player, amount);
            }

            public boolean canTakeItems(PlayerEntity playerEntity) {
                return invokeCanTakeOutput(playerEntity, this.hasStack());
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                invokeOnTakeOutput(player, stack);
            }
        };
    }
}