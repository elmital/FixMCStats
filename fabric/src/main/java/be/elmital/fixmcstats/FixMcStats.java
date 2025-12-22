package be.elmital.fixmcstats;

import net.fabricmc.api.ModInitializer;

public class FixMcStats implements ModInitializer {
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        // TODO change
        CommonClass.init();

        // TODO
        /*
        LOGGER.info("Initializing mod");
        try {
            LOGGER.info("Checking mod config...");
            Config.initConfig(LOGGER);
            LOGGER.info("Mod configured!");
            LOGGER.info("Checking for custom statistics");
            StatisticUtils.registerAllCustomStats(LOGGER);
            LOGGER.info("Handling command event for potential server side purpose...");
            BasicCommand.register();
            LOGGER.info("Commands registered!");
            LOGGER.info("Registering command arguments...");
            BasicCommand.registerArgumentTypes();
            LOGGER.info("Command arguments registered!");
        } catch (SecurityException e) {
            LOGGER.trace("Can't load or generate config file! There is a read/write permission issue.", e);
            throw new RuntimeException(e);
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.trace("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Mod initialized!");
        */

        // TODO client side command registration
        /*
        LOGGER.info("Registering commands client side...");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(BasicCommand.commandNodeBuilder(ModEnvironment.CLIENT, (fabricClientCommandSource, text, aBoolean) -> fabricClientCommandSource.sendFeedback(text))));
        LOGGER.info("Commands registered!");
        */
    }
}
