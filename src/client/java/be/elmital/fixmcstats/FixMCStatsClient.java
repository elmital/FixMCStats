package be.elmital.fixmcstats;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixMCStatsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fix-mc-stats");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing mod");
		LOGGER.info("Mod initialized!");
	}
}