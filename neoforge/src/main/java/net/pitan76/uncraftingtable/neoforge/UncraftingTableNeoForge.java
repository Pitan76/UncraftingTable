package net.pitan76.uncraftingtable.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.neoforge.client.UncraftingTableNeoForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    public UncraftingTableNeoForge(ModContainer modContainer) {
        modContainer.getEventBus().addListener(UncraftingTableNeoForge::init);
        modContainer.getEventBus().addListener(UncraftingTableNeoForgeClient::clientInit);
    }

    public static void init(FMLCommonSetupEvent event) {
        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        UncraftingTable.init();
    }
}