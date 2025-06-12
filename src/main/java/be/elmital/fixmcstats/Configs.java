package be.elmital.fixmcstats;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Configs {
    public static ArrayList<ConfigEntry> configEntries = new ArrayList<>();

    public static ArrayList<ConfigEntry> getValidConfigEntries() {
        return configEntries.stream().filter(configEntry -> !configEntry.isDeprecated()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConfigEntry> getAllPatchConfigEntries() {
        return configEntries.stream().filter(ConfigEntry::isAPatchConfig).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ConfigEntry registerEntry(ConfigEntry configEntry) {
        configEntries.add(configEntry);
        return configEntry;
    }

    public static ConfigEntry ELYTRA_FIX = registerEntry(new ConfigEntry("elytra-experimental-fix", Boolean.TRUE.toString(), true, false, false));
    public static ConfigEntry ENDER_DRAGON_FLOWN_STAT_FIX = registerEntry(new ConfigEntry("ender-dragon-flown-stat-experimental-fix", Boolean.TRUE.toString(), true, false, false));
    public static ConfigEntry STATS_SCREEN_TICK_FIX = registerEntry(new ConfigEntry("pause-tick-on-stats-screen-experimental-fix", Boolean.TRUE.toString(), true, true, false));
    public static ConfigEntry CAMEL_STAT = registerEntry(new ConfigEntry("use-camel-riding-stat", Boolean.TRUE.toString(), true, false));
    public static ConfigEntry CRAWL_STAT = registerEntry(new ConfigEntry("use-crawling-stat", Boolean.TRUE.toString(), true, false));


    public static class ConfigEntry {
        private final String key, def;
        private final boolean patch, experimental, deprecated;
        private boolean active;

        protected ConfigEntry(String key, String def, boolean patch, boolean experimental) {
            this(key, def, patch, experimental, false);
        }

        protected ConfigEntry(String key, String def, boolean patch, boolean experimental, boolean deprecated) {
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
            return this.patch;
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
    }
}
