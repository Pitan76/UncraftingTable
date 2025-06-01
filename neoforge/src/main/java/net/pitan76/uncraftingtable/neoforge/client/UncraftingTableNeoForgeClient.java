package net.pitan76.uncraftingtable.neoforge.client;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.pitan76.uncraftingtable.UncraftingTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;

public class UncraftingTableNeoForgeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger("uncraftingtable76");
    
    public static void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Initializing client through event...");
            try {
                UncraftingTableClient.init();
                LOGGER.info("Client initialization completed successfully");
            } catch (Exception e) {
                LOGGER.error("Client initialization failed: {}", e.getMessage());
                LOGGER.error("Exception stack trace", e);
            }
        });
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        LOGGER.info("Registering NeoForge menu screen - Menu Type: {}:uncraftingtable", UncraftingTable.MOD_ID);
        try {
            UncraftingTableClient.registerScreens();

            LOGGER.info("NeoForge menu screen registered successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to register NeoForge menu screen: {}", e.getMessage());
            LOGGER.error("Exception stack trace", e);
        }
    }
}
