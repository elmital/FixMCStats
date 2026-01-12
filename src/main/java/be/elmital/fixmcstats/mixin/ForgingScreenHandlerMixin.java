package be.elmital.fixmcstats.mixin;

import be.elmital.fixmcstats.Configs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("unused")
@Mixin(ItemCombinerMenu.class)
public abstract class ForgingScreenHandlerMixin extends AbstractContainerMenu {
    @Final
    @Shadow
    protected Player player;
    @Invoker("mayPickup")
    public abstract boolean invokeCanTakeOutput(Player player, boolean present);
    @Invoker("onTake")
    public abstract void invokeOnTakeOutput(Player player, ItemStack stack);

    @Final
    @Shadow
    protected ResultContainer resultSlots;
    protected ForgingScreenHandlerMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    // Fix https://bugs.mojang.com/browse/MC-65198 for Smithing table
    @ModifyArg(method = "createResultSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ItemCombinerMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;"))
    private Slot modifySlot(Slot slot, @Local(argsOnly = true) ItemCombinerMenuSlotDefinition manager) {
        if (!Configs.CRAFT_STAT_CLICKING_FIX.isActive())
            return slot;
        return new Slot(this.resultSlots, manager.getResultSlot().slotIndex(), manager.getResultSlot().x(), manager.getResultSlot().y()) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public void on(ItemStack stack, int amount) {
                if (((ItemCombinerMenu) (Object) ForgingScreenHandlerMixin.this) instanceof SmithingMenu)
                    stack.onCraftedBy(player.level(), player, amount);
            }

            public boolean mayPickup(Player playerEntity) {
                return invokeCanTakeOutput(playerEntity, this.hasItem());
            }

            public void onTake(Player player, ItemStack stack) {
                invokeOnTakeOutput(player, stack);
            }
        };
    }
}