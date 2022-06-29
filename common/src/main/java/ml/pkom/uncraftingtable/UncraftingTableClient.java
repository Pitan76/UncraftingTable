package ml.pkom.uncraftingtable;

import dev.architectury.registry.menu.MenuRegistry;

public class UncraftingTableClient {
    public static void init() {
        MenuRegistry.registerScreenFactory(UncraftingTable.supplierUNCRAFTING_TABLE_MENU.getOrNull(), UncraftingScreen::new);
    }
}
