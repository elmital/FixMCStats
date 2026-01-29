package be.elmital.fixmcstats.platform;

import be.elmital.fixmcstats.Constants;
import be.elmital.fixmcstats.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/*
    Service loaders are a built-in Java feature that allow us to locate implementations of an interface that vary from
    one environment to another. In this context we use this feature to access a mock API in the common code that
    is swapped out for the platform specific implementation at runtime.
 */
public class Services {

     // We provide a platform helper which provides information about what platform the mod is running on.
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    /*
        This code is used to load a service for the current environment. The implementation of the service is defined
        manually by including a text file in META-INF/services named with the fully qualified class name of the service.
        For example our file on NeoForge points to NeoForgePlatformHelper while Fabric points to FabricPlatformHelper.
     */
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, clazz.getClassLoader()) // Force this specific class loader to prevent potential conflicts with other mods and allows it to be cached safely @see ServiceLoader#load(Class) API notes
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName() + " using the classloader " + clazz.getClassLoader().getName()));
        Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}