package be.elmital.fixmcstats;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.RegisterEvent;

import java.io.IOException;

@Mod(FixMCStats.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FixMCStats {
    public static final String MODID = "fix_mc_stats";

    public FixMCStats(FMLJavaModLoadingContext context) {
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