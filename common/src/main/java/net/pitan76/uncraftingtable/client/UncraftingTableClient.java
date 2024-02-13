package net.pitan76.uncraftingtable.client;

import dev.architectury.registry.menu.MenuRegistry;
import net.pitan76.uncraftingtable.UncraftingScreen;
import net.pitan76.uncraftingtable.UncraftingTable;

public class UncraftingTableClient {
    public static void init() {
        MenuRegistry.registerScreenFactory(UncraftingTable.supplierUNCRAFTING_TABLE_MENU.getOrNull(), UncraftingScreen::new);
    }
}
