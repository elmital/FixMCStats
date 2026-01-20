package be.elmital.fixmcstats;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class FixMcStatsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
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