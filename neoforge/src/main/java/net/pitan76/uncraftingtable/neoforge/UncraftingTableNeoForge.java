package net.pitan76.uncraftingtable.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.pitan76.uncraftingtable.neoforge.client.UncraftingTableNeoForgeClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    public UncraftingTableNeoForge(IEventBus bus) {
        Config.init(FMLPaths.CONFIGDIR.get().toFile());

        UncraftingTable.init();
        if (Platform.getEnvironment() == Env.CLIENT)
            bus.addListener(UncraftingTableNeoForgeClient::clientInit);
    }
}