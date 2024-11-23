package permdog99.steam_integration;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class SteamIntegrationClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SteamIntegration.LOGGER.info("Initializing Client-side Steam Integration...");

		SteamIntegration.LOGGER.info("Setting up Steam API...");
		this.tryLoadSteamAPI();
		// Begins ticking the Steam API
		ClientTickEvents.START_CLIENT_TICK.register(this::tickSteamAPI);

		// Stops Steam API and functions
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> stopSteamAPIFunctions());

		SteamIntegration.LOGGER.info("Client-side Steam Integration has been initialized!");
    }

	public void tryLoadSteamAPI() {
		try {
			SteamAPI.loadLibraries();
			if (!SteamAPI.init()) {
				// Steamworks initialization error, e.g. Steam client not running
				SteamIntegration.LOGGER.warn("Steam is not running!");
			} else {
				SteamIntegration.LOGGER.info("Steam API is initialized!");
			}
		} catch (SteamException e) {
			// Error extracting or loading native libraries
			SteamIntegration.LOGGER.warn("Cannot find native Steam API libraries; either failed loading or extracting!");
		}
	}

	private void tickSteamAPI(Minecraft client) {
		if (SteamAPI.isSteamRunning()) {
			SteamAPI.runCallbacks();
		}
	}

	public static void stopSteamAPIFunctions() {
		SteamAPI.shutdown();
		SteamIntegration.LOGGER.info("Stutting down Steam API... Done!");
	}
}