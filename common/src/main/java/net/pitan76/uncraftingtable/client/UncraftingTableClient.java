package net.pitan76.uncraftingtable.client;

import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.uncraftingtable.UncraftingTable;

public class UncraftingTableClient {
    public static void init() {
        CompatRegistryClient.registerScreen(UncraftingTable.UNCRAFTING_TABLE_MENU.getOrNull(), UncraftingScreen::new);
    }
}
