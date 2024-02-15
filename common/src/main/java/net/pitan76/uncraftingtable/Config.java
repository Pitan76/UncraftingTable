package net.pitan76.uncraftingtable;

import net.pitan76.easyapi.FileControl;
import net.pitan76.easyapi.config.JsonConfig;

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
        config.setBoolean("restore_enchantment_book", true);
        config.setBoolean("disable_uncrafting_uu_matter", false);
        if (!FileControl.fileExists(getConfigFile())) {
            config.save(getConfigFile());
        }
        config.load(getConfigFile());
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
