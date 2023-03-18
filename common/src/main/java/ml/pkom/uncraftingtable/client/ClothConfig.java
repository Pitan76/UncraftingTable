package ml.pkom.uncraftingtable.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pkom.easyapi.FileControl;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.client.gui.screen.Screen;

import static ml.pkom.uncraftingtable.Config.*;

public class ClothConfig {
    public static Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(TextUtil.translatable("title.uncraftingtable76.config"))
                .setSavingRunnable(() -> {
                    if (!FileControl.fileExists(getConfigDir())) {
                        getConfigDir().mkdirs();
                    }
                    config.save(getConfigFile());
                });
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(TextUtil.translatable("category.uncraftingtable76.general"));
        general.addEntry(entryBuilder.startIntField(TextUtil.translatable("option.uncraftingtable76.consume_xp"), config.getInt("consume_xp"))
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.setInt("consume_xp", newValue))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(TextUtil.translatable("option.uncraftingtable76.uncraft_damaged_item"), config.getBoolean("uncraft_damaged_item"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.setBoolean("uncraft_damaged_item", newValue))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(TextUtil.translatable("option.uncraftingtable76.restore_enchantment_book"), config.getBoolean("restore_enchantment_book"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.setBoolean("restore_enchantment_book", newValue))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(TextUtil.translatable("option.uncraftingtable76.disable_uncrafting_uu_matter"), config.getBoolean("disable_uncrafting_uu_matter"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.setBoolean("disable_uncrafting_uu_matter", newValue))
                .build());
        return builder.build();
    }
}
