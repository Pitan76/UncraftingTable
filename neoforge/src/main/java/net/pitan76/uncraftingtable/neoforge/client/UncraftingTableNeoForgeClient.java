package net.pitan76.uncraftingtable.neoforge.client;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;

public class UncraftingTableNeoForgeClient {
    public static void clientInit(FMLClientSetupEvent event) {
        UncraftingTableClient.init();
    }
}
