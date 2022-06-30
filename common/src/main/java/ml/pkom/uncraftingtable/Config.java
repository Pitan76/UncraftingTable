package ml.pkom.uncraftingtable;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pkom.uncraftingtable.easyapi.FileControl;
import ml.pkom.uncraftingtable.easyapi.config.JsonConfig;
import net.minecraft.client.gui.screen.Screen;

import java.io.File;

public class Config {

    public static File configDir;
    public static String fileName = UncraftingTable.MOD_ID + ".json";

    public static JsonConfig config = new JsonConfig();

    public static boolean initialized = false;

    public static void init(File configDir) {
        System.out.println("[Uncrafting Table]" + configDir.toString());
        if (initialized) return;
        initialized = true;
        setConfigDir(configDir);
        config.setInt("consume_xp", 0);
        config.setBoolean("uncraft_damaged_item", true);
        if (!FileControl.fileExists(getConfigFile())) {
            config.save(getConfigFile());
        }
        config.load(getConfigFile());
    }

    public static Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(Utils.translatableText("title.uncraftingtable76.config"))
                .setSavingRunnable(() -> {
                    if (!FileControl.fileExists(getConfigDir())) {
                        getConfigDir().mkdirs();
                    }
                    config.save(getConfigFile());
                });
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Utils.translatableText("category.uncraftingtable76.general"));
        general.addEntry(entryBuilder.startIntField(Utils.translatableText("option.uncraftingtable76.consume_xp"), config.getInt("consume_xp"))
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.setInt("consume_xp", newValue))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Utils.translatableText("option.uncraftingtable76.uncraft_damaged_item"), config.getBoolean("uncraft_damaged_item"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.setBoolean("uncraft_damaged_item", newValue))
                .build());
        return builder.build();
    }

    public static File getConfigFile() {
        return new File(getConfigDir(), fileName);
    }

    public static void setConfigDir(File configDir) {
        Config.configDir = configDir;
    }

    public static File getConfigDir() {
        return configDir;
    }
}
