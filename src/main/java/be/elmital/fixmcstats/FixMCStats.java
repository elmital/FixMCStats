package be.elmital.fixmcstats;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Mod(FixMCStats.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FixMCStats {
    public static final String MODID = "fix_mc_stats";

    public static final Logger LOGGER = LoggerFactory.getLogger("FixMCStats");

    public FixMCStats(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Initializing mod");
        try {
            LOGGER.info("Checking mod config...");
            Config.initConfig(LOGGER);
            LOGGER.info("Mod configured!");
        } catch (SecurityException e) {
            LOGGER.error("Can't load or generate config file! There is a read/write permission issue.", e);
            throw new RuntimeException(e);
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error("Can't load or generate config! If the file exists already delete it and it will be re-created on the next launch with the default values.", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Mod initialized!");
    }

    @SubscribeEvent
    public static void onRegisterStats(RegisterEvent event) {
        if (event.getRegistryKey().equals(BuiltInRegistries.CUSTOM_STAT.key())) {
            LOGGER.info("Checking for custom statistics");
            StatisticUtils.registerAllCustomStats(Config.instance(), LOGGER);
        }
    }
}