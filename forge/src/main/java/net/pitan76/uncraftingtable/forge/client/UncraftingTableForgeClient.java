package net.pitan76.uncraftingtable.forge.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;

public class UncraftingTableForgeClient {
    public static void clientInit(FMLClientSetupEvent event) {
        UncraftingTableClient.init();
    }
}
