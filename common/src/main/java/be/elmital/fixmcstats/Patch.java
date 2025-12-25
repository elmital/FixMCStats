package be.elmital.fixmcstats;

import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;

public record Patch(int id, ModEnvironment environment) {
    public static Patch of(int id, ModEnvironment environment) {
        return new Patch(id, environment);
    }
    public String getPatchId() {
        return "MC-" + this.id;
    }

    public @Nullable URI getIssueLink() {
        try {
            return new URI("https://bugs.mojang.com/browse/" + getPatchId());
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
