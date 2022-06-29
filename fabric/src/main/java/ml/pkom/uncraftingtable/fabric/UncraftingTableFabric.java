package ml.pkom.uncraftingtable.fabric;

import ml.pkom.uncraftingtable.UncraftingTable;
import ml.pkom.uncraftingtable.UncraftingTableClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class UncraftingTableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        UncraftingTable.init();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            UncraftingTableClient.init();
        }
    }
}