package net.pitan76.uncraftingtable.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.forge.client.UncraftingTableForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableForge {

    @SuppressWarnings("removal")
    public UncraftingTableForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(UncraftingTable.MOD_ID, bus);
        bus.addListener(UncraftingTableForgeClient::clientInit);

        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        new UncraftingTable();
    }
}