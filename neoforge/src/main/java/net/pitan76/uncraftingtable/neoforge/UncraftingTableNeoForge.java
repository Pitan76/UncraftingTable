package net.pitan76.uncraftingtable.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.neoforge.client.UncraftingTableNeoForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    public UncraftingTableNeoForge(ModContainer modContainer) {
        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        IEventBus modEventBus = modContainer.getEventBus();

        if (modEventBus == null)
            throw new IllegalStateException("modEventBus is null");

        UncraftingTable.init();
        modEventBus.addListener(UncraftingTableNeoForgeClient::clientInit);
    }

    /*
    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(UncraftingTable.UNCRAFTING_TABLE_MENU.get(), UncraftingScreen::new);
    }
    */
}