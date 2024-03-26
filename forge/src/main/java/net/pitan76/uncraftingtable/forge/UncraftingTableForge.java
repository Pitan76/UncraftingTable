package net.pitan76.uncraftingtable.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.forge.client.UncraftingTableForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableForge {

    public UncraftingTableForge() {
        this(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public UncraftingTableForge(IEventBus bus) {
        bus.addListener(UncraftingTableForge::init);
        bus.addListener(UncraftingTableForgeClient::clientInit);

        EventBuses.registerModEventBus(UncraftingTable.MOD_ID, bus);
    }

    public static void init(FMLCommonSetupEvent event) {
        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        UncraftingTable.init();
    }
}