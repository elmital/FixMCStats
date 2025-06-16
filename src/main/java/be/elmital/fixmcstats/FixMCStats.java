package be.elmital.fixmcstats;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
			StatisticUtils.registerAllCustomStats(LOGGER);
			LOGGER.info("Handling command event for potential server side purpose...");
			ConfigCommand.registerConfigCommand();
			LOGGER.info("Commands registered!");
			LOGGER.info("Registering command arguments...");
			ConfigCommand.registerArgumentTypes();
			LOGGER.info("Command arguments registered!");
		} catch (SecurityException e) {
			LOGGER.trace("Can't load or generate config file! There is a read/write permission issue.", e);
			throw new RuntimeException(e);
		} catch (IOException | IllegalArgumentException e) {
			LOGGER.trace("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
			throw new RuntimeException(e);
		}
		LOGGER.info("Mod initialized!");
	}
}