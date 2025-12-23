package be.elmital.fixmcstats;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class FixMCStats {

    public FixMCStats(IEventBus eventBus) {
        CommonClass.init();
    }
}