package be.elmital.fixmcstats.mixinlogic;

import be.elmital.fixmcstats.Configs;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;

public class LivingEntity {
    public static void checkAwarding(net.minecraft.world.entity.LivingEntity livingEntity, ItemStack elytra) {
        if (!Configs.BREAKING_ELYTRA_AND_TRIDENT_FIX.isActive())
            return;
        if (!ElytraItem.isFlyEnabled(elytra)) {
            if (livingEntity instanceof Player playerEntity)
                playerEntity.awardStat(Stats.ITEM_BROKEN.get(elytra.getItem()));
        }
    }
}
