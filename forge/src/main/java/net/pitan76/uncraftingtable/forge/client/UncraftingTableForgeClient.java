package net.pitan76.uncraftingtable.forge.client;

import net.pitan76.uncraftingtable.client.UncraftingTableClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class UncraftingTableForgeClient {
    public static void clientInit(FMLClientSetupEvent event) {
        UncraftingTableClient.init();
    }
}
