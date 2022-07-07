package ml.pkom.uncraftingtable.forge.client;

import ml.pkom.uncraftingtable.client.UncraftingTableClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class UncraftingTableForgeClient {
    public static void clientInit(FMLClientSetupEvent event) {
        UncraftingTableClient.init();
    }
}
