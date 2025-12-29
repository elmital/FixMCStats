package be.elmital.fixmcstats.utils;

import be.elmital.fixmcstats.Constants;
import be.elmital.fixmcstats.mixin.StatsAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;

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
        Registry.register(BuiltInRegistries.CUSTOM_STAT, statistic.identifier(), statistic.identifier());
        StatsAccessor.getCUSTOM().get(statistic.identifier(), statistic.statFormatter());
    }

    public static void registerAllCustomStats() {
        Constants.LOGGER.info("Checking for custom statistics...");
        Constants.LOGGER.info("Adding camel custom stat to the registry");
        StatisticUtils.register(StatisticUtils.CAMEL_RIDING_STAT);
        Constants.LOGGER.info("Adding donkey custom stat to the registry");
        StatisticUtils.register(StatisticUtils.DONKEY_RIDING_STAT);
        Constants.LOGGER.info("Adding mule custom stat to the registry");
        StatisticUtils.register(StatisticUtils.MULE_RIDING_STAT);
        Constants.LOGGER.info("Adding crawling custom stat to the registry");
        StatisticUtils.register(StatisticUtils.CRAWL_ONE_CM);
        Constants.LOGGER.info("All custom statistics added");
    }

    public record CustomStatistic(String path, StatFormatter statFormatter, Identifier identifier) {
        public CustomStatistic(String path, StatFormatter statFormatter) {
            this(path, statFormatter, Identifier.fromNamespaceAndPath("fix-mc-stats", path));
        }
    }
}
