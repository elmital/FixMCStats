package be.elmital.fixmcstats.mixinlogic;

import be.elmital.fixmcstats.Configs;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LivingEntity {
    // Fix https://bugs.mojang.com/browse/MC-122656
    public static void awardElytraBrokenStat(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        if (!Configs.BREAKING_ELYTRA_AND_TRIDENT_FIX.isActive())
            return;

        if (stack.nextDamageWillBreak() && entity instanceof Player playerEntity)
            playerEntity.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
    }
}
