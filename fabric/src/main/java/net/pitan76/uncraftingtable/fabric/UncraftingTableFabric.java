package net.pitan76.uncraftingtable.fabric;

import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class UncraftingTableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Config.init(FabricLoader.getInstance().getConfigDir().toFile());
        UncraftingTable.init();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            UncraftingTableClient.init();
    }
}