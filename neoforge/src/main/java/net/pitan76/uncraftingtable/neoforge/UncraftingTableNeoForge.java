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
        IEventBus bus = modContainer.getEventBus();

        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        if (bus == null) throw new IllegalStateException("bus is null");

        new UncraftingTable();

        bus.addListener(UncraftingTableNeoForgeClient::clientInit);
    }
}