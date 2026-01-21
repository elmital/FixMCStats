package be.elmital.fixmcstats;

import be.elmital.fixmcstats.utils.StatisticUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class FixMcStats implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();

        StatisticUtils.registerAllCustomStats();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> BasicCommand.registerServerSide(dispatcher));

        BasicCommand.notifyArgumentRegisteringStarting();
        ArgumentTypeRegistry.registerArgumentType(BasicCommand.PATCH_ACTION_ARGUMENT.id, BasicCommand.PATCH_ACTION_ARGUMENT.clazz, BasicCommand.PATCH_ACTION_ARGUMENT.serializer);
        ArgumentTypeRegistry.registerArgumentType(BasicCommand.PATCH_ARGUMENT.id, BasicCommand.PATCH_ARGUMENT.clazz, BasicCommand.PATCH_ARGUMENT.serializer);
        BasicCommand.notifyArgumentRegisteringEnding();
    }
}
