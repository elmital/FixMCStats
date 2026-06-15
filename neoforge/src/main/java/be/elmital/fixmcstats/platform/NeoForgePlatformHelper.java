package be.elmital.fixmcstats.platform;

import be.elmital.fixmcstats.platform.services.IPlatformHelper;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public boolean isDedicatedServer() {
        return FMLLoader.getCurrent().getDist().isDedicatedServer();
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void registerArgumentType(Identifier id, Class<A> clazz, ArgumentTypeInfo<A, T> serializer) {
        ArgumentTypeInfos.registerByClass(clazz, serializer);
        Registry.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, id, serializer);
    }
}