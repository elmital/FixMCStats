package be.elmital.fixmcstats;

import net.fabricmc.api.ModInitializer;

public class FixMcStats implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
