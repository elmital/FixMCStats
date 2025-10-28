package be.elmital.fixmcstats;

import be.elmital.fixmcstats.mixin.StatsAccessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

/* Used to register non-vanilla statistics and concern :
   - https://bugs.mojang.com/browse/MC-148457
   - https://bugs.mojang.com/browse/MC-256638
   - https://bugs.mojang.com/browse/MC-277294
 */
public class StatisticUtils {
    public static final CustomStatistic CAMEL_RIDING_STAT = new CustomStatistic("camel_one_cm", StatFormatter.DISTANCE);
    public static final CustomStatistic CRAWL_ONE_CM = new CustomStatistic("crawl_one_cm", StatFormatter.DISTANCE);
    public static final CustomStatistic DONKEY_RIDING_STAT = new CustomStatistic("donkey_one_cm", StatFormatter.DISTANCE);
    public static final CustomStatistic MULE_RIDING_STAT = new CustomStatistic("mule_one_cm", StatFormatter.DISTANCE);

    public static void register(CustomStatistic statistic) {
        Registry.register(Registries.CUSTOM_STAT, statistic.identifier(), statistic.identifier());
        StatsAccessor.getCUSTOM().getOrCreateStat(statistic.identifier(), statistic.statFormatter());
    }

    public static void registerAllCustomStats(Logger logger) {
        if (Configs.CAMEL_STAT.isActive()) {
            logger.info("Adding camel custom stat to the registry");
            StatisticUtils.register(StatisticUtils.CAMEL_RIDING_STAT);
        }

        if (Configs.USE_DONKEY_STATS.isActive()) {
            logger.info("Adding donkey custom stat to the registry");
            StatisticUtils.register(StatisticUtils.DONKEY_RIDING_STAT);
        }

        if (Configs.USE_MULE_STATS.isActive()) {
            logger.info("Adding mule custom stat to the registry");
            StatisticUtils.register(StatisticUtils.MULE_RIDING_STAT);
        }

        if (Configs.CRAWL_STAT.isActive()) {
            logger.info("Adding crawling custom stat to the registry");
            StatisticUtils.register(StatisticUtils.CRAWL_ONE_CM);
        }
    }

    public record CustomStatistic(String path, StatFormatter statFormatter, Identifier identifier) {
        public CustomStatistic(String path, StatFormatter statFormatter) {
            this(path, statFormatter, Identifier.of("fix-mc-stats", path));
        }
    }
}
