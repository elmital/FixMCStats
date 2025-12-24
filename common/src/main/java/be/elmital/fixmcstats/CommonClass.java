package be.elmital.fixmcstats;

import be.elmital.fixmcstats.platform.Services;
import be.elmital.fixmcstats.utils.StatisticUtils;

import java.io.IOException;


public class CommonClass {
    public static void init() {
        Constants.LOGGER.info("Initializing mod cross platform part");
        try {
            Constants.LOGGER.info("Checking mod config...");
            Config.initConfig(Constants.LOGGER, Services.PLATFORM.getConfigDir());
            Constants.LOGGER.info("Mod configured!");
            Constants.LOGGER.info("Checking for custom statistics");
            StatisticUtils.registerAllCustomStats(Constants.LOGGER);
        } catch (SecurityException e) {
            Constants.LOGGER.trace("Can't load or generate config file! There is a read/write permission issue.", e);
            throw new RuntimeException(e);
        } catch (IOException | IllegalArgumentException e) {
            Constants.LOGGER.trace("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
            throw new RuntimeException(e);
        }
        Constants.LOGGER.info("Mod cross platform part initialized!");
    }
}