package be.elmital.fixmcstats;


import be.elmital.fixmcstats.utils.StatisticUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
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
        event.register(BuiltInRegistries.CUSTOM_STAT.key(), registry -> {
            StatisticUtils.registerAllCustomStats();
        });

        event.register(Registries.COMMAND_ARGUMENT_TYPE, registry -> {
            /*
                Hacky way to register the serializer we can't use the BasicCommand.PATCH_ARGUMENT built one cause the method to register them is parameterized and don't allow this.
                The method used by Fabric for the ArgumentType registration delegates to the vanilla method that is private and not accessible here.
             */
            BasicCommand.notifyArgumentRegisteringStarting();
            var serializer = ArgumentTypeInfos.registerByClass(BasicCommand.PatchArgumentType.class, SingletonArgumentInfo.contextAware(access -> BasicCommand.PatchArgumentType.patchArgument(null)));
            ArgumentTypeInfos.registerByClass(BasicCommand.PatchActionArgumentType.class, BasicCommand.PATCH_ACTION_ARGUMENT.serializer);
            registry.register(BasicCommand.PATCH_ARGUMENT.id, serializer);
            registry.register(BasicCommand.PATCH_ACTION_ARGUMENT.id, BasicCommand.PATCH_ACTION_ARGUMENT.serializer);
            BasicCommand.notifyArgumentRegisteringEnding();
        });
    }
}