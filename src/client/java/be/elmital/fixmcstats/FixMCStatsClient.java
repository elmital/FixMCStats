package be.elmital.fixmcstats;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FixMCStatsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fix-mc-stats");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Registering commands client side...");
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ConfigCommand.commandNodeBuilder(ModEnvironment.CLIENT, (fabricClientCommandSource, text, aBoolean) -> fabricClientCommandSource.sendFeedback(text))));
		LOGGER.info("Commands registered!");
	}
}