package net.pitan76.uncraftingtable.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.client.UncraftingScreen;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    public UncraftingTableNeoForge(ModContainer modContainer) {
        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        IEventBus bus = modContainer.getEventBus();

        if (bus == null)
            throw new IllegalStateException("modEventBus is null");

        new UncraftingTable();

        //modEventBus.addListener(UncraftingTableNeoForgeClient::clientInit);
        bus.addListener(this::registerScreens);
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(UncraftingTable.UNCRAFTING_TABLE_MENU.get(), UncraftingScreen::new);
    }
}