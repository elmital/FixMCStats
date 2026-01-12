package be.elmital.fixmcstats;

import be.elmital.fixmcstats.mixin.StatsAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
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
        Registry.register(BuiltInRegistries.CUSTOM_STAT, statistic.identifier(), statistic.identifier());
        StatsAccessor.getCUSTOM().get(statistic.identifier(), statistic.statFormatter());
    }

    public static void registerAllCustomStats(Logger logger) {
        logger.info("Adding camel custom stat to the registry");
        StatisticUtils.register(StatisticUtils.CAMEL_RIDING_STAT);
        logger.info("Adding donkey custom stat to the registry");
        StatisticUtils.register(StatisticUtils.DONKEY_RIDING_STAT);
        logger.info("Adding mule custom stat to the registry");
        StatisticUtils.register(StatisticUtils.MULE_RIDING_STAT);
        logger.info("Adding crawling custom stat to the registry");
        StatisticUtils.register(StatisticUtils.CRAWL_ONE_CM);
    }

    public record CustomStatistic(String path, StatFormatter statFormatter, ResourceLocation identifier) {
        public CustomStatistic(String path, StatFormatter statFormatter) {
            this(path, statFormatter, ResourceLocation.fromNamespaceAndPath("fix-mc-stats", path));
        }
    }
}
