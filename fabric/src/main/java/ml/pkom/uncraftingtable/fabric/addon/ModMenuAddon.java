package ml.pkom.uncraftingtable.fabric.addon;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ml.pkom.uncraftingtable.client.ClothConfig;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuAddon implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config") || FabricLoader.getInstance().isModLoaded("cloth_config"))
            return ClothConfig::create;
        else
            return null;
    }
}
