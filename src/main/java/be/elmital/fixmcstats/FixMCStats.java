package be.elmital.fixmcstats;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class FixMCStats implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("fix-mc-stats");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing mod");
		try {
			LOGGER.info("Checking mod config...");
			Config.initConfig(LOGGER);
			LOGGER.info("Mod configured!");
			LOGGER.info("Checking for custom statistics");
			StatisticUtils.registerAllCustomStats(Config.instance(), LOGGER);
		} catch (IOException | URISyntaxException e) {
			LOGGER.trace("Can't load or generate config!", e);
			throw new RuntimeException(e);
		}
		LOGGER.info("Mod initialized!");
	}
}