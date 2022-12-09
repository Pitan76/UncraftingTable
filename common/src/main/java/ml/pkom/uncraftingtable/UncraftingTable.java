package ml.pkom.uncraftingtable;

import dev.architectury.networking.NetworkManager;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.registry.RegistryEvent;
import ml.pkom.mcpitanlibarch.api.item.DefaultItemGroups;
import ml.pkom.mcpitanlibarch.api.item.ExtendSettings;
import ml.pkom.mcpitanlibarch.api.registry.ArchRegistry;
import net.minecraft.item.BlockItem;
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
        registry.registerItem(id("uncraftingtable"), () -> new BlockItem(UncraftingTableBlock.UNCRAFTING_TABLE, new ExtendSettings().addGroup(DefaultItemGroups.DECORATIONS, id("uncraftingtable"))));
        supplierUNCRAFTING_TABLE_MENU = registry.registerScreenHandlerType(id("uncraftingtable"), () -> new ScreenHandlerType<>(UncraftingScreenHandler::new));

        UncraftingScreenHandler.init();

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, id("network"), ((buf, context) -> {
            NbtCompound nbt = buf.readNbt();
            if (nbt.contains("control")) {
                Player player = new Player(context.getPlayer());
                int ctrl = nbt.getInt("control");
                if (ctrl == 0) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.getSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.getSlot(0);
                        if (slot.getStack().isEmpty()) return;
                        slot.removeRecipeIndex();
                    }
                }
                if (ctrl == 1) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.getSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.getSlot(0);
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
