package be.elmital.fixmcstats;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixMCStats implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("fix-mc-stats");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing mod");
		LOGGER.info("Mod initialized!");
	}
}