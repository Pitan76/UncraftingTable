package ml.pkom.uncraftingtable;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.registry.RegistryEvent;
import ml.pkom.mcpitanlibarch.api.gui.SimpleScreenHandlerTypeBuilder;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import ml.pkom.mcpitanlibarch.api.item.DefaultItemGroups;
import ml.pkom.mcpitanlibarch.api.network.ServerNetworking;
import ml.pkom.mcpitanlibarch.api.registry.ArchRegistry;
import ml.pkom.mcpitanlibarch.api.util.ItemUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UncraftingTable {

    public static final String MOD_ID = "uncraftingtable76";
    public static final String MOD_NAME = "UncraftingTable";

    public static Logger LOGGER = LogManager.getLogger();
    public static void log(Level level, String message){
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static final ArchRegistry registry = ArchRegistry.createRegistry(MOD_ID);

    public static RegistryEvent<ScreenHandlerType<?>> supplierUNCRAFTING_TABLE_MENU;

    public static void init() {
        registry.registerBlock(id("uncraftingtable"), () -> UncraftingTableBlock.UNCRAFTING_TABLE);
        registry.registerItem(id("uncraftingtable"), () -> ItemUtil.ofBlock(UncraftingTableBlock.UNCRAFTING_TABLE, CompatibleItemSettings.of()
                // 1.19.3～
                .addGroup(DefaultItemGroups.FUNCTIONAL, id("uncraftingtable"))
                // ～1.19.2
                .addGroup(DefaultItemGroups.DECORATIONS)
                )
        );
        supplierUNCRAFTING_TABLE_MENU = registry.registerScreenHandlerType(id("uncraftingtable"), () -> new SimpleScreenHandlerTypeBuilder<>(UncraftingScreenHandler::new).build());

        UncraftingScreenHandler.init();

        ServerNetworking.registerReceiver(id("network"), ((server, p, buf) -> {
            NbtCompound nbt = buf.readNbt();
            if (nbt.contains("control")) {
                Player player = new Player(p);
                int ctrl = nbt.getInt("control");
                if (ctrl == 0) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.callGetSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.callGetSlot(0);
                        if (slot.getStack().isEmpty()) return;
                        slot.removeRecipeIndex();
                    }
                }
                if (ctrl == 1) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.callGetSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.callGetSlot(0);
                        if (slot.getStack().isEmpty()) return;
                        slot.addRecipeIndex();
                    }
                }
            }
        }));
        registry.allRegister();
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }
}
