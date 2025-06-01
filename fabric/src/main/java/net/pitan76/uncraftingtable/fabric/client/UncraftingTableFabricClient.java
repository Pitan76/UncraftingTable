package net.pitan76.uncraftingtable.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;

public class UncraftingTableFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Config.init(FabricLoader.getInstance().getConfigDir().toFile());
        UncraftingTableClient.init();
        UncraftingTableClient.registerScreens();
    }
}