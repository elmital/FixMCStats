package be.elmital.fixmcstats;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static Config INSTANCE;
    private final Logger logger;
    private final Path currentDirectory;
    private final Properties properties = new Properties();
    static final String CONFIG = "FixMCStatsConfig";

    public static Config instance() {
        return INSTANCE;
    }

    public static void initConfig(Logger logger) throws IOException, SecurityException, IllegalArgumentException {
        INSTANCE = new Config(logger).loadOrGenerateConfig();
    }

    Config(Logger logger) {
        this.logger = logger;
        currentDirectory = FabricLoader.getInstance().getConfigDir();
    }

    Config loadOrGenerateConfig() throws IOException, SecurityException, IllegalArgumentException {
        boolean alreadyExist = Files.exists(getConfigPath());

        if (alreadyExist) {
            logger.info("Config file found loading it");
            InputStream input = new FileInputStream(getConfigPath().toString());
            properties.load(input);
            logger.info("File loaded inspecting config keys...");
            input.close();
        } else {
            logger.info("No config file found creating it...");
        }

        boolean toStore = !alreadyExist;
        for (Configs.ConfigEntry value : Configs.configEntries) {
            if (value.isDeprecated()) {
                if (alreadyExist && properties.containsKey(value.getKey())) {
                    logger.info("Old config key found '{}' removing it", value.getKey());
                    toStore = true;
                    properties.remove(value.getKey());
                }
                continue;
            }

            if (!alreadyExist || !properties.containsKey(value.getKey())) {
                if (alreadyExist)
                    logger.info("Adding missing config key {}", value.getKey());
                toStore = true;
                properties.setProperty(value.getKey(), value.getDefault());
            }
        }

        if (!toStore)
            logger.info("All keys are setup correctly");

        if (toStore) {
            FileOutputStream stream = new FileOutputStream(getConfigPath().toString());
            logger.info("Saving file...");
            properties.store(stream, null);
            logger.info("Saved!");
            stream.close();
        }

        logger.info("Loading all configs");
        for (Configs.ConfigEntry configEntry : Configs.getValidConfigEntries()) {
            configEntry.setActive(Boolean.parseBoolean(properties.getProperty(configEntry.getKey(), configEntry.getDefault())));
        }
        return this;
    }

    public Path getConfigDirectoryPath() {
        return this.currentDirectory;
    }

    public @NotNull Path getConfigPath() {
        return getConfigDirectoryPath().resolve(CONFIG);
    }

    @SuppressWarnings("unused")
    public void updateConfig(Configs.ConfigEntry config, String value) throws IOException {
        var stream = new FileOutputStream(getConfigPath().toString());
        properties.setProperty(config.getKey(), value);
        properties.store(stream, null);
        stream.close();
    }

    @SuppressWarnings("unused")
    public void removeFromConfig(String key) throws IOException {
        var stream = new FileOutputStream(getConfigPath().toString());
        properties.remove(key);
        properties.store(stream, null);
        stream.close();
    }
}
