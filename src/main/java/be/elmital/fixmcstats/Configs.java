package be.elmital.fixmcstats;

import net.fabricmc.loader.api.metadata.ModEnvironment;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Configs {
    public static ArrayList<ConfigEntry> configEntries = new ArrayList<>();

    public static ArrayList<ConfigEntry> getValidConfigEntries() {
        return configEntries.stream().filter(configEntry -> !configEntry.isDeprecated()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConfigEntry> getAllPatchConfigEntries(@Nullable ModEnvironment environment) {
        return configEntries.stream().filter(ConfigEntry::isAPatchConfig).filter(configEntry -> environment == null || (configEntry.patch != null && configEntry.patch.environment().equals(environment))).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConfigEntry> getAllValidPatchConfigEntries(@Nullable ModEnvironment environment) {
        return configEntries.stream().filter(configEntry -> !configEntry.isDeprecated() && configEntry.isAPatchConfig() && (environment == null || (configEntry.patch != null && configEntry.patch.environment().equals(environment)))).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ConfigEntry registerEntry(ConfigEntry configEntry) {
        configEntries.add(configEntry);
        return configEntry;
    }

    public static ConfigEntry STATS_SCREEN_TICK_FIX = registerEntry(new ConfigEntry("pause-tick-on-stats-screen-experimental-fix", Boolean.TRUE.toString(), Patch.of(36696, ModEnvironment.SERVER), false, true));
    public static ConfigEntry CAMEL_STAT = registerEntry(new ConfigEntry("use-camel-riding-stat", Boolean.TRUE.toString(), Patch.of(148457, ModEnvironment.SERVER), false));
    public static ConfigEntry CRAWL_STAT = registerEntry(new ConfigEntry("use-crawling-stat", Boolean.TRUE.toString(), Patch.of(256638, ModEnvironment.SERVER), false));
    public static ConfigEntry ELYTRA_FIX = registerEntry(new ConfigEntry("elytra-experimental-fix", Boolean.TRUE.toString(), Patch.of(259687, ModEnvironment.SERVER), false, true));
    public static ConfigEntry ENDER_DRAGON_FLOWN_STAT_FIX = registerEntry(new ConfigEntry("ender-dragon-flown-stat-experimental-fix", Boolean.TRUE.toString(), Patch.of(267006, ModEnvironment.SERVER), false, false));


    public static class ConfigEntry {
        private final String key, def;
        private final boolean experimental, deprecated;
        private final @Nullable Patch patch;
        private boolean active;

        protected ConfigEntry(String key, String def, Patch patch, boolean experimental) {
            this(key, def, patch, experimental, false);
        }

        protected ConfigEntry(String key, String def, @Nullable Patch patch, boolean experimental, boolean deprecated) {
            this.key = key;
            this.def = def;
            this.patch = patch;
            this.experimental = experimental;
            this.deprecated = deprecated;
        }

        public String getKey() {
            return this.key;
        }

        public String getDefault() {
            return this.def;
        }

        public boolean isAPatchConfig() {
            return this.patch != null;
        }

        public String getPatchId() {
            return patch != null ? patch.getPatchId() : null;
        }

        public URI getPatchLink() {
            return patch != null ? patch.getIssueLink() : null;
        }

        public boolean isExperimental() {
            return this.experimental;
        }

        public boolean isDeprecated() {
            return this.deprecated;
        }

        public boolean isActive() {
            return !this.deprecated && this.active;
        }

        ConfigEntry setActive(boolean active) {
            this.active = active;
            return this;
        }

        ConfigEntry updateActive(boolean active) throws IOException {
            Config.instance().updateConfig(this, String.valueOf(active));
            return setActive(active);
        }
    }
}
