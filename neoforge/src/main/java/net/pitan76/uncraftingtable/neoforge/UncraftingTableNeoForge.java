package net.pitan76.uncraftingtable.neoforge;

import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;
import net.pitan76.uncraftingtable.neoforge.client.UncraftingTableNeoForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    private static final Logger LOGGER = LoggerFactory.getLogger("uncraftingtable76");

    public UncraftingTableNeoForge(ModContainer modContainer) {
        IEventBus bus = modContainer.getEventBus();

        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        if (bus == null) throw new IllegalStateException("bus is null");

        new UncraftingTable();

        // Registering client event listener
        bus.addListener(UncraftingTableNeoForgeClient::clientInit);
        
        // Registering menu screen event listener
        if (PlatformUtil.isClient()) {
            LOGGER.info("Registering menu screen event listener");
            bus.addListener(UncraftingTableNeoForgeClient::registerMenuScreens);
        }
        
        // isClient, initialize client directly
        if (PlatformUtil.isClient()) {
            LOGGER.info("Attempting to initialize client directly...");
            try {
                // Marking as NeoForge environment
                //UncraftingTableClient.markAsNeoForge();
                UncraftingTableClient.init();
                LOGGER.info("Client initialization completed successfully");
            } catch (Exception e) {
                LOGGER.error("Client initialization failed: {}", e.getMessage());
                LOGGER.error("Exception stack trace", e);
            }
        }
    }
}