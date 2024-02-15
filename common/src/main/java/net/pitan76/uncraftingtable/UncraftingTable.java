package net.pitan76.uncraftingtable;

import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.util.ItemUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.pitan76.mcpitanlib.api.registry.CompatRegistry;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
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

    public static final CompatRegistry registry = CompatRegistry.createRegistry(MOD_ID);

    public static RegistryResult<ScreenHandlerType<?>> supplierUNCRAFTING_TABLE_MENU;

    public static void init() {
        registry.registerBlock(id("uncraftingtable"), () -> UncraftingTableBlock.UNCRAFTING_TABLE);
        registry.registerItem(id("uncraftingtable"), () -> ItemUtil.ofBlock(UncraftingTableBlock.UNCRAFTING_TABLE, CompatibleItemSettings.of()
                // 1.19.3～
                .addGroup(() -> DefaultItemGroups.FUNCTIONAL, id("uncraftingtable"))
                // ～1.19.2
                .addGroup(DefaultItemGroups.DECORATIONS)
                )
        );
        supplierUNCRAFTING_TABLE_MENU = registry.registerScreenHandlerType(id("uncraftingtable"), () -> new SimpleScreenHandlerTypeBuilder<>(UncraftingScreenHandler::new).build());

        UncraftingScreenHandler.init();

        ServerNetworking.registerReceiver(id("network"), ((server, p, buf) -> {
            NbtCompound nbt = PacketByteUtil.readNbt(buf);
            if (nbt.contains("control")) {
                Player player = new Player(p);
                int ctrl = nbt.getInt("control");
                if (ctrl == 0) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.callGetSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.callGetSlot(0);
                        if (slot.callGetStack().isEmpty()) return;
                        slot.removeRecipeIndex();
                    }
                }
                if (ctrl == 1) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.callGetSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.callGetSlot(0);
                        if (slot.callGetStack().isEmpty()) return;
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
