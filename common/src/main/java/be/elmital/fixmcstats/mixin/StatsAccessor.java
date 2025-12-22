package be.elmital.fixmcstats.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Stats.class)
public interface StatsAccessor {
    @Accessor
    static StatType<Identifier> getCUSTOM() {
        throw new RuntimeException();
    }
}
