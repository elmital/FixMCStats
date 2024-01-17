package be.elmital.fixmcstats;

import be.elmital.fixmcstats.mixin.StatsAccessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

/* Used to register non-vanilla statistics and concern :
   - https://bugs.mojang.com/browse/MC-256638
 */
public class StatisticUtils {
    public static CustomStatistic CAMEL_RIDING_STAT = new CustomStatistic("camel_one_cm", StatFormatter.DISTANCE);

    public static void register(CustomStatistic statistic) {
        Registry.register(Registries.CUSTOM_STAT, statistic.identifier(), statistic.identifier());
        StatsAccessor.getCUSTOM().getOrCreateStat(statistic.identifier(), statistic.statFormatter());
    }

    public static void registerAllCustomStats(Config config, Logger logger) {
        if (config.USE_CAMEL_CUSTOM_STAT) {
            logger.info("Adding camel custom stat to the registry");
            StatisticUtils.register(StatisticUtils.CAMEL_RIDING_STAT);
        }
    }

    public record CustomStatistic(String path, StatFormatter statFormatter, Identifier identifier) {
        public CustomStatistic(String path, StatFormatter statFormatter) {
            this(path, statFormatter, new Identifier("fix-mc-stats", path));
        }
    }
}
