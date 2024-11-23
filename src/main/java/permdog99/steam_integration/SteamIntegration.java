package permdog99.steam_integration;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamGameServerAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteamIntegration implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("mc-steam-integration");
    public static final String VERSION = /*$ mod_version*/ "0.1.0";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Initializing Server-side Steam Integration...");

        //? if !release
        LOGGER.warn("Not on a release build of MC! Be warned!");

        //? if fapi: <0.95 {
        LOGGER.info("Fabric API is old on this version");
        LOGGER.info("Please update!");
        //?}

        LOGGER.info("Setting up Steam GameServer API...");
        this.tryLoadSteamGameServerAPI();
        ServerTickEvents.START_SERVER_TICK.register(this::tickSteamGameServerAPI);
        ServerLifecycleEvents.SERVER_STARTED.register(server -> stopSteamGameServerAPIFunctions());
        LOGGER.info("Server-side Steam Integration has been initialized!");
    }

    public void tryLoadSteamGameServerAPI() {
        try {
            SteamGameServerAPI.loadLibraries();
            if (!SteamGameServerAPI.init((127 << 24) + 1, (short) 27016, (short) 27017,
                    SteamGameServerAPI.ServerMode.NoAuthentication, "0.0.1")) {
                // initialization error
                LOGGER.warn("Steam GameServer API has failed to initialize!");
            } else {
                LOGGER.info("Steam GameServer API is initialized!");
            }
        } catch (SteamException e) {
            // Error extracting or loading native libraries
            LOGGER.warn("Cannot find native Steam GameServer API libraries; either failed loading or extracting!");
        }
    }

    public void tickSteamGameServerAPI(MinecraftServer server) {
        while (server.isRunning()) {
            SteamGameServerAPI.runCallbacks();
        }
    }

    public void stopSteamGameServerAPIFunctions() {
        SteamGameServerAPI.shutdown();
    }
}