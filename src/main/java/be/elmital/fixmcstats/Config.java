package be.elmital.fixmcstats;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static Config INSTANCE;
    private final Logger logger;
    private final Path currentDirectory;
    private final Properties properties = new Properties();
    public boolean ELYTRA_EXPERIMENTAL_FIX;
    public boolean USE_CAMEL_CUSTOM_STAT;
    static String CONFIG = "FixMCStatsConfig";

    public static Config instance() {
        return INSTANCE;
    }

    public static Config initConfig(Logger logger) throws IOException, URISyntaxException {
        return INSTANCE = new Config(logger).loadOrGenerateConfig();
    }

    Config(Logger logger) {
        this.logger = logger;
        currentDirectory = FabricLoader.getInstance().getConfigDir();
    }

    public enum Configs {
        ELYTRA_FIX("elytra-experimental-fix", Boolean.TRUE.toString()),
        CAMEL_STAT("use-camel-riding-stat", Boolean.TRUE.toString());

        private final String key;
        private final String def;

        Configs(String key, String def) {
            this.key = key;
            this.def = def;
        }

        public String getKey() {
            return key;
        }

        public String getDefault() {
            return def;
        }
    }

    Config loadOrGenerateConfig() throws IOException, IllegalArgumentException {
        boolean alreadyExist = Files.exists(getConfigPath());

        if (alreadyExist) {
            logger.info("Config file found loading it");
            InputStream input = new FileInputStream(getConfigPath().toString());
            properties.load(input);
            logger.info("File loaded checking for missing configs");
            input.close();
        } else {
            logger.info("No config file found creating it...");
        }
        FileOutputStream stream = new FileOutputStream(getConfigPath().toString());

        boolean toStore = !alreadyExist;
        for (Configs value : Configs.values()) {
            if (!alreadyExist || !properties.containsKey(value.getKey())) {
                if (alreadyExist)
                    logger.info("Adding missing config " + value.getKey());
                toStore = true;
                properties.setProperty(value.getKey(), value.getDefault());
            }
        }
        if (toStore) {
            logger.info("Saving file...");
            properties.store(stream, null);
            logger.info("Saved");
        }
        stream.close();

        logger.info("Loading all configs");
        ELYTRA_EXPERIMENTAL_FIX = Boolean.parseBoolean(properties.getProperty(Configs.ELYTRA_FIX.getKey(), Configs.ELYTRA_FIX.getDefault()));
        USE_CAMEL_CUSTOM_STAT = Boolean.parseBoolean(properties.getProperty(Configs.CAMEL_STAT.getKey(), Configs.CAMEL_STAT.getDefault()));
        return this;
    }

    public Path getConfigDirectoryPath() {
        return this.currentDirectory;
    }

    public Path getConfigPath() {
        return getConfigDirectoryPath().resolve(CONFIG);
    }

    public void updateConfig(Configs config, String value) throws IOException {
        var stream = new FileOutputStream(getConfigPath().toString());
        properties.setProperty(config.getKey(), value);
        properties.store(stream, null);
    }

    public void removeFromConfig(String key) throws IOException {
        var stream = new FileOutputStream(getConfigPath().toString());
        properties.remove(key);
        properties.store(stream, null);
    }
}
