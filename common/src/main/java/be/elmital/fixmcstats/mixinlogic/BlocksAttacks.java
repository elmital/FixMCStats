package be.elmital.fixmcstats.mixinlogic;

import be.elmital.fixmcstats.Configs;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;

public class BlocksAttacks {

    // https://bugs.mojang.com/browse/MC-298805
    public static void checkAwardingStat(Player instance, Stat<?> stat, float damages) {
        if (!Configs.SHIELD_USED_FIX.isActive() || damages > 0.0F) {
            instance.awardStat(stat);
        }
    }
}
