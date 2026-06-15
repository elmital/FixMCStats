package be.elmital.fixmcstats;


import be.elmital.fixmcstats.platform.Services;
import be.elmital.fixmcstats.utils.StatisticUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("fixmcstats")
@EventBusSubscriber
public class FixMCStats {

    public FixMCStats(IEventBus eventBus) {
        CommonClass.init();
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (Configs.DISALLOW_COMMANDS.isActive() && Services.PLATFORM.isDedicatedServer()) {
            Constants.LOGGER.info("Commands are disallowed, skip registration");
            return;
        }
        BasicCommand.registerServerSide(event.getDispatcher());
    }

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        BasicCommand.registerClientSide(event.getDispatcher(), new BasicCommand<>() {
            @Override
            public void sendClientFeedBack(CommandSourceStack clientCommandSource, Component text) {
                clientCommandSource.sendSystemMessage(text);
            }
        });
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.CUSTOM_STAT.key(), registry -> StatisticUtils.registerAllCustomStats());

        event.register(Registries.COMMAND_ARGUMENT_TYPE, registry -> {
            if (Configs.DISALLOW_COMMANDS.isActive() && Services.PLATFORM.isDedicatedServer()) {
                Constants.LOGGER.info("Commands are disallowed, skip argument type registration");
                return;
            }
            BasicCommand.registerServerSideArguments();
        });
    }
}