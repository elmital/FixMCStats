package be.elmital.fixmcstats;

import net.fabricmc.api.ClientModInitializer;

public class FixMCStatsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		System.out.println("HELLO WORLD");
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}