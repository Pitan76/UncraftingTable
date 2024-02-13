package net.pitan76.uncraftingtable.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.forge.client.UncraftingTableForgeClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableForge {
    public UncraftingTableForge() {
        EventBuses.registerModEventBus(UncraftingTable.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Config.init(FMLPaths.CONFIGDIR.get().toFile());

        UncraftingTable.init();
        if (Platform.getEnvironment() == Env.CLIENT)
            FMLJavaModLoadingContext.get().getModEventBus().addListener(UncraftingTableForgeClient::clientInit);

    }
}