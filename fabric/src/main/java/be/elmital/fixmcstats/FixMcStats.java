package be.elmital.fixmcstats;

import be.elmital.fixmcstats.utils.StatisticUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;

public class FixMcStats implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();

        StatisticUtils.registerAllCustomStats(Constants.LOGGER);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> BasicCommand.registerServerSide(dispatcher));

        BasicCommand.notifyArgumentRegisteringStarting();
        ArgumentTypeRegistry.registerArgumentType(BasicCommand.PATCH_ACTION_ARGUMENT.id, BasicCommand.PATCH_ACTION_ARGUMENT.clazz, BasicCommand.PATCH_ACTION_ARGUMENT.serializer);
        ArgumentTypeRegistry.registerArgumentType(BasicCommand.PATCH_ARGUMENT.id, BasicCommand.PATCH_ARGUMENT.clazz, BasicCommand.PATCH_ARGUMENT.serializer);
        BasicCommand.notifyArgumentRegisteringEnding();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                BasicCommand.registerClientSide(dispatcher, new BasicCommand<>() {
                    @Override
                    public void sendClientFeedBack(FabricClientCommandSource clientCommandSource, Component text) {
                        clientCommandSource.sendFeedback(text);
                    }
                })
        );
    }
}
