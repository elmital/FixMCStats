package be.elmital.fixmcstats;

import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static Config INSTANCE = null;

    private final Path currentDirectory;
    static String CONFIG = "FixMCStatsConfig";
    private final Properties properties = new Properties();

    public static Config instance() throws IOException, URISyntaxException {
        if (INSTANCE == null)
            INSTANCE = new Config();
        return INSTANCE;
    }

    Config() throws IOException {
        currentDirectory = FabricLoader.getInstance().getConfigDir();
        loadOrGenerateConfig();
    }

    public enum Configs {
        ;
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

    public void loadOrGenerateConfig() throws IOException, IllegalArgumentException {
        if (Files.exists(getConfigPath())) {
            InputStream input = new FileInputStream(getConfigPath().toString());
            properties.load(input);
        } else {
            var stream = new FileOutputStream(getConfigPath().toString());

            for (Configs value : Configs.values()) {
                properties.setProperty(value.getKey(), value.getDefault());
            }
            properties.store(stream, null);
        }
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
