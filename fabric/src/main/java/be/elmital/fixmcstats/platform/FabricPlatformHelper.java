package be.elmital.fixmcstats.platform;

import be.elmital.fixmcstats.platform.services.IPlatformHelper;
import com.mojang.brigadier.arguments.ArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isDedicatedServer() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER);
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void registerArgumentType(ResourceLocation id, Class<A> clazz, ArgumentTypeInfo<A, T> serializer) {
        ArgumentTypeRegistry.registerArgumentType(id, clazz, serializer);
    }
}
