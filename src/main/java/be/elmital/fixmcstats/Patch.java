package be.elmital.fixmcstats;

import net.fabricmc.loader.api.metadata.ModEnvironment;

public record Patch(int id, ModEnvironment environment) {
    public static Patch of(int id, ModEnvironment environment) {
        return new Patch(id, environment);
    }
    public String getPatchId() {
        return "MC-" + this.id;
    }
}
