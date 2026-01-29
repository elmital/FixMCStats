package be.elmital.fixmcstats.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * Gets the path to the directory where mods configurations are stored
     * @return The Path of the config directory
     */
    Path getConfigDir();

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Check if the mode is running on a dedicated server
     *
     * @return true if in a dedicated server, false otherwise.
     */
    boolean isDedicatedServer();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}