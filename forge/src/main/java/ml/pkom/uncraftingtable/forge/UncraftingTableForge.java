package ml.pkom.uncraftingtable.forge;

import dev.architectury.platform.forge.EventBuses;
import ml.pkom.uncraftingtable.UncraftingTable;
import ml.pkom.uncraftingtable.UncraftingTableClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableForge {
    public UncraftingTableForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(UncraftingTable.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        UncraftingTable.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);

    }

    public void clientInit(FMLClientSetupEvent event) {
        UncraftingTableClient.init();
    }
}