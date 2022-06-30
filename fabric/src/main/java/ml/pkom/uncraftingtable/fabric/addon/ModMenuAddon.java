package ml.pkom.uncraftingtable.fabric.addon;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ml.pkom.uncraftingtable.Config;

public class ModMenuAddon implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //parent -> Config.create(parent);
        return Config::create;
    }
}
