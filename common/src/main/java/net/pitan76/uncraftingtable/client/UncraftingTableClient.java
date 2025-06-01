package net.pitan76.uncraftingtable.client;

import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.uncraftingtable.UncraftingTable;

public class UncraftingTableClient {
    public static void init() {

    }

    public static void registerScreens() {
        CompatRegistryClient.registerScreen(UncraftingTable.MOD_ID, UncraftingTable.UNCRAFTING_TABLE_MENU.get(), UncraftingScreen::new);
    }
}
