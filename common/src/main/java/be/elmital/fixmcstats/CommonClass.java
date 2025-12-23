package be.elmital.fixmcstats;

import be.elmital.fixmcstats.platform.Services;

import java.io.IOException;


public class CommonClass {
    public static void init() {
        Constants.LOGGER.info("Initializing mod");
        try {
            Constants.LOGGER.info("Checking mod config...");
            Config.initConfig(Constants.LOGGER, Services.PLATFORM.getConfigDir());
            Constants.LOGGER.info("Mod configured!");
            // TODO
            // Constants.LOGGER.info("Checking for custom statistics");
            // StatisticUtils.registerAllCustomStats(LOGGER);
            Constants.LOGGER.info("Handling command event for potential server side purpose...");
            // TODO
            // BasicCommand.register();
            Constants.LOGGER.info("Commands registered!");
            Constants.LOGGER.info("Registering command arguments...");
            // TODO client side command registration
            /*
            Constants.LOGGER.info("Registering commands client side...");
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(BasicCommand.commandNodeBuilder(ModEnvironment.CLIENT, (fabricClientCommandSource, text, aBoolean) -> fabricClientCommandSource.sendFeedback(text))));
            Constants.LOGGER.info("Commands registered!");
            */
            // TODO
            // BasicCommand.registerArgumentTypes();
            Constants.LOGGER.info("Command arguments registered!");
        } catch (SecurityException e) {
            Constants.LOGGER.trace("Can't load or generate config file! There is a read/write permission issue.", e);
            throw new RuntimeException(e);
        } catch (IOException | IllegalArgumentException e) {
            Constants.LOGGER.trace("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
            throw new RuntimeException(e);
        }
        Constants.LOGGER.info("Mod initialized!");




        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("examplemod")) {

            Constants.LOGGER.info("Hello to examplemod");
        }
    }
}