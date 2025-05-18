package be.elmital.fixmcstats;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.io.IOException;

@Mod(FixMCStats.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FixMCStats {
    public static final String MODID = "fix_mc_stats";

    public FixMCStats(IEventBus modEventBus, ModContainer modContainer) {
        StatisticUtils.LOGGER.info("Initializing mod");
        try {
            StatisticUtils.LOGGER.info("Checking mod config...");
            Config.initConfig(FMLPaths.CONFIGDIR.get());
            StatisticUtils.LOGGER.info("Mod configured!");
        } catch (SecurityException e) {
            StatisticUtils.LOGGER.error("Can't load or generate config file! There is a read/write permission issue.", e);
            throw new RuntimeException(e);
        } catch (IOException | IllegalArgumentException e) {
            StatisticUtils.LOGGER.error("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
            throw new RuntimeException(e);
        }
        StatisticUtils.LOGGER.info("Mod initialized!");
    }

    @SubscribeEvent
    public static void onRegisterStats(RegisterEvent event) {
        if (event.getRegistryKey().equals(BuiltInRegistries.CUSTOM_STAT.key())) {
            StatisticUtils.LOGGER.info("Checking for custom statistics");
            StatisticUtils.registerAllCustomStats(Config.instance());
        }
    }
}