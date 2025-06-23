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

    public static ConfigEntry DAMAGE_DEALT_WITH_PROJECTILE_FIX = registerEntry(new ConfigEntry("damage-dealt-with-projectile-fix", Boolean.TRUE.toString(), Patch.of(29519, ModEnvironment.SERVER), false, false));
    public static ConfigEntry STATS_SCREEN_TICK_FIX = registerEntry(new ConfigEntry("pause-tick-on-stats-screen-experimental-fix", Boolean.TRUE.toString(), Patch.of(36696, ModEnvironment.SERVER), true, true));
    public static ConfigEntry CRAFT_STAT_CLICKING_FIX = registerEntry(new ConfigEntry("craft-stats-clicking-fix", Boolean.TRUE.toString(), Patch.of(65198, ModEnvironment.SERVER), false, false));
    public static ConfigEntry MISSING_SPACE_STATS_SCREEN = registerEntry(new ConfigEntry("missing-space-stats-screen-fix", Boolean.TRUE.toString(), Patch.of(80827, ModEnvironment.CLIENT), false, false));
    public static ConfigEntry DAMAGE_DEALT_WITH_SWEEPING_FIX = registerEntry(new ConfigEntry("damage-dealt-sweeping-fix", Boolean.TRUE.toString(), Patch.of(111435, ModEnvironment.SERVER), false, false));
    public static ConfigEntry RARE_BLOCKS_COLORS = registerEntry(new ConfigEntry("rare-blocks-colors-fix", Boolean.TRUE.toString(), Patch.of(139386, ModEnvironment.CLIENT), false, false));
    public static ConfigEntry DISTANCE_FALLEN_ON_LANDING_FIX = registerEntry(new ConfigEntry("distance-fallen-on-landing-fix", Boolean.TRUE.toString(), Patch.of(121541, ModEnvironment.SERVER), false, false));
    public static ConfigEntry BREAKING_ELYTRA_AND_TRIDENT_FIX = registerEntry(new ConfigEntry("breaking-elytra-and-trident-fix", Boolean.TRUE.toString(), Patch.of(122656, ModEnvironment.SERVER), false, false));
    public static ConfigEntry CAMP_FIRE_COOKING_CRAFT_STAT_FIX = registerEntry(new ConfigEntry("campfire-cooking-craft-stat-fix", Boolean.TRUE.toString(), Patch.of(144005, ModEnvironment.SERVER), false, false));
    public static ConfigEntry IGNITED_CREEPER_FIX = registerEntry(new ConfigEntry("ignited-creeper-fix", Boolean.TRUE.toString(), Patch.of(147347, ModEnvironment.SERVER), false, false));
    public static ConfigEntry CAMEL_STAT = registerEntry(new ConfigEntry("use-camel-riding-stat", Boolean.TRUE.toString(), Patch.of(148457, ModEnvironment.SERVER), false));
    public static ConfigEntry CRAFTING_POTION_FIX = registerEntry(new ConfigEntry("crafting-potion-fix", Boolean.TRUE.toString(), Patch.of(154487, ModEnvironment.SERVER), false, false));
    public static ConfigEntry GLOWSTONE_USED_ON_ANCHOR_FIX = registerEntry(new ConfigEntry("glowstone-used-on-anchor-fix", Boolean.TRUE.toString(), Patch.of(176806, ModEnvironment.SERVER), false, false));
    public static ConfigEntry SORTED_STATS_SCREEN = registerEntry(new ConfigEntry("sorted-stats-screen-fix", Boolean.TRUE.toString(), Patch.of(178516, ModEnvironment.CLIENT), false, false));
    public static ConfigEntry JUMP_WHEN_CLIMBING_SCAFFOLDING_FIX = registerEntry(new ConfigEntry("jump-when-climbing-fix", Boolean.TRUE.toString(), Patch.of(211938, ModEnvironment.SERVER), false, false));
    public static ConfigEntry ITEM_SORTING_FIX = registerEntry(new ConfigEntry("item-sorting-stats-screen-fix", Boolean.TRUE.toString(), Patch.of(213103, ModEnvironment.CLIENT), false, false));
    public static ConfigEntry BREAKING_CROSSBOW_FIX = registerEntry(new ConfigEntry("breaking-crossbow-fix", Boolean.TRUE.toString(), Patch.of(214457, ModEnvironment.SERVER), false, false));
    public static ConfigEntry FLOWER_POTTED_FIX = registerEntry(new ConfigEntry("flower-potted-fix", Boolean.TRUE.toString(), Patch.of(231743, ModEnvironment.SERVER), false, false));
    public static ConfigEntry TIMES_MINED_BLOCKS_FIX = registerEntry(new ConfigEntry("times-mined-blocks-fix", Boolean.TRUE.toString(), Patch.of(245962, ModEnvironment.SERVER), false, false));
    public static ConfigEntry CRAWL_STAT = registerEntry(new ConfigEntry("use-crawling-stat", Boolean.TRUE.toString(), Patch.of(256638, ModEnvironment.SERVER), false));
    public static ConfigEntry ELYTRA_FIX = registerEntry(new ConfigEntry("elytra-experimental-fix", Boolean.TRUE.toString(), Patch.of(259687, ModEnvironment.SERVER), false, true));
    public static ConfigEntry PLACEABLE_ON_WATER_FIX = registerEntry(new ConfigEntry("placeable-on-water-fix", Boolean.TRUE.toString(), Patch.of(264274, ModEnvironment.SERVER), false, false));
    public static ConfigEntry KILLED_BY_GOAT_FIX = registerEntry(new ConfigEntry("killed-by-goat-fix", Boolean.TRUE.toString(), Patch.of(265376, ModEnvironment.SERVER), false, false));
    public static ConfigEntry ENDER_DRAGON_FLOWN_STAT_FIX = registerEntry(new ConfigEntry("ender-dragon-flown-stat-experimental-fix", Boolean.TRUE.toString(), Patch.of(267006, ModEnvironment.SERVER), false, true));
    public static ConfigEntry DECORATED_POT_BREAKING = registerEntry(new ConfigEntry("decorated-pot-breaking-fix", Boolean.TRUE.toString(), Patch.of(268093, ModEnvironment.SERVER), false, false));
    public static ConfigEntry EQUIP_ARMOR_STAND_FIX = registerEntry(new ConfigEntry("equip-armor-stand-fix", Boolean.TRUE.toString(), Patch.of(273933, ModEnvironment.SERVER), false, false));
    public static ConfigEntry USE_DONKEY_STATS = registerEntry(new ConfigEntry("use-donkey-stat", Boolean.TRUE.toString(), Patch.of(277294, ModEnvironment.SERVER), false, false));
    public static ConfigEntry USE_MULE_STATS = registerEntry(new ConfigEntry("use-mule-stat", Boolean.TRUE.toString(), Patch.of(277294, ModEnvironment.SERVER), false, false));


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
