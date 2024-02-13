package net.pitan76.uncraftingtable.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.forge.client.UncraftingTableForgeClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableForge {
    public UncraftingTableForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(UncraftingTable.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Config.init(FMLPaths.CONFIGDIR.get().toFile());

        UncraftingTable.init();
        if (Platform.getEnv().isClient())
            FMLJavaModLoadingContext.get().getModEventBus().addListener(UncraftingTableForgeClient::clientInit);

    }
}