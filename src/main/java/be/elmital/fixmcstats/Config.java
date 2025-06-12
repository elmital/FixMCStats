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
    public boolean USE_CAMEL_CUSTOM_STAT;
    public boolean USE_CRAWL_CUSTOM_STAT;
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

    public enum Configs {
        ELYTRA_FIX("elytra-experimental-fix", Boolean.TRUE.toString(), true),
        CAMEL_STAT("use-camel-riding-stat", Boolean.TRUE.toString()),
        CRAWL_STAT("use-crawling-stat", Boolean.TRUE.toString()),
        ENDER_DRAGON_FLOWN_STAT_FIX("ender-dragon-flown-stat-experimental-fix", Boolean.TRUE.toString(), true),
        EXPERIMENTAL_STATS_SCREEN_TICK_FIX("pause-tick-on-stats-screen-experimental-fix", Boolean.TRUE.toString(), true);

        private final String key;
        private final String def;
        private final boolean deprecated;

        Configs(String key, String def) {
            this(key, def, false);
        }

        Configs(String key, String def, boolean deprecated) {
            this.key = key;
            this.def = def;
            this.deprecated = deprecated;
        }

        public String getKey() {
            return key;
        }

        public String getDefault() {
            return def;
        }
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
        for (Configs value : Configs.values()) {
            if (value.deprecated) {
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
        USE_CAMEL_CUSTOM_STAT = Boolean.parseBoolean(properties.getProperty(Configs.CAMEL_STAT.getKey(), Configs.CAMEL_STAT.getDefault()));
        USE_CRAWL_CUSTOM_STAT = Boolean.parseBoolean(properties.getProperty(Configs.CRAWL_STAT.getKey(), Configs.CRAWL_STAT.getDefault()));
        return this;
    }

    public Path getConfigDirectoryPath() {
        return this.currentDirectory;
    }

    public @NotNull Path getConfigPath() {
        return getConfigDirectoryPath().resolve(CONFIG);
    }

    @SuppressWarnings("unused")
    public void updateConfig(Configs config, String value) throws IOException {
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
