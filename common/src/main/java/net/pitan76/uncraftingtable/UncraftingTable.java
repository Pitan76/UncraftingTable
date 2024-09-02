package net.pitan76.uncraftingtable;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.ItemUtil;
import net.pitan76.mcpitanlib.api.util.Logger;

public class UncraftingTable {

    public static final String MOD_ID = "uncraftingtable76";
    public static final String MOD_NAME = "UncraftingTable";

    public static Logger LOGGER = new Logger(MOD_NAME);

    public static final CompatRegistryV2 registry = CompatRegistryV2.create(MOD_ID);

    public static RegistryResult<ScreenHandlerType<?>> UNCRAFTING_TABLE_MENU;
    public static RegistryResult<Block> UNCRAFTING_TABLE;

    public static void init() {
        UNCRAFTING_TABLE = registry.registerBlock(id("uncraftingtable"), () -> UncraftingTableBlock.UNCRAFTING_TABLE);
        registry.registerItem(id("uncraftingtable"), () -> ItemUtil.ofBlock(UNCRAFTING_TABLE.getOrNull(), CompatibleItemSettings.of()
                // 1.19.3～
                .addGroup(() -> DefaultItemGroups.FUNCTIONAL, id("uncraftingtable").toMinecraft())
                // ～1.19.2
                .addGroup(DefaultItemGroups.DECORATIONS)
                )
        );
        UNCRAFTING_TABLE_MENU = registry.registerScreenHandlerType(id("uncraftingtable"), () -> new SimpleScreenHandlerTypeBuilder<>(UncraftingScreenHandler::new).build());

        UncraftingScreenHandler.init();

        ServerNetworking.registerReceiver(id("network").toMinecraft(), ((server, p, buf) -> {
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
                        slot.prevRecipeIndex();
                    }
                }
                if (ctrl == 1) {
                    if (!(player.getCurrentScreenHandler() instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.getCurrentScreenHandler();
                    if (screenHandler.callGetSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.callGetSlot(0);
                        if (slot.callGetStack().isEmpty()) return;
                        slot.nextRecipeIndex();
                    }
                }
            }
        }));

        registry.allRegister();
    }

    public static CompatIdentifier id(String id) {
        return CompatIdentifier.of(MOD_ID, id);
    }

    public static void log(String message){
        LOGGER.info("[" + MOD_NAME + "] " + message);
    }
}
