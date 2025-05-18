package be.elmital.fixmcstats;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;

public class FixMCStats implements ModInitializer {
    public static final String MODID = "fix_mc_stats";

    @Override
    public void onInitialize() {
        StatisticUtils.LOGGER.info("Initializing mod");
        try {
            StatisticUtils.LOGGER.info("Checking mod config...");
            Config.initConfig(FabricLoader.getInstance().getConfigDir());
            StatisticUtils.LOGGER.info("Mod configured!");
            StatisticUtils.LOGGER.info("Checking for custom statistics");
            StatisticUtils.registerAllCustomStats(Config.instance());
        } catch (SecurityException e) {
            StatisticUtils.LOGGER.error("Can't load or generate config file! There is a read/write permission issue.", e);
            throw new RuntimeException(e);
        } catch (IOException | IllegalArgumentException e) {
            StatisticUtils.LOGGER.error("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
            throw new RuntimeException(e);
        }
        StatisticUtils.LOGGER.info("Mod initialized!");
    }
}