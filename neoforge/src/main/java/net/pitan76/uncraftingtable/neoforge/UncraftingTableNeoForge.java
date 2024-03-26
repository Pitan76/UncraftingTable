package net.pitan76.uncraftingtable.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.neoforge.client.UncraftingTableNeoForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    public UncraftingTableNeoForge(IEventBus bus) {
        bus.addListener(UncraftingTableNeoForge::init);
        bus.addListener(UncraftingTableNeoForgeClient::clientInit);
    }

    public static void init(FMLCommonSetupEvent event) {
        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        UncraftingTable.init();
    }
}