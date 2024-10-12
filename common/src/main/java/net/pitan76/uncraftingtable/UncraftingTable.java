package net.pitan76.uncraftingtable;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.mcpitanlib.api.CommonModInitializer;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.DefaultItemGroups;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ServerNetworking;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.registry.result.SupplierResult;
import net.pitan76.mcpitanlib.api.registry.v2.CompatRegistryV2;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.ItemUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;

public class UncraftingTable extends CommonModInitializer {

    public static final String MOD_ID = "uncraftingtable76";
    public static final String MOD_NAME = "UncraftingTable";

    public static CompatRegistryV2 registry;
    public static UncraftingTable INSTANCE;

    public static SupplierResult<ScreenHandlerType<UncraftingScreenHandler>> UNCRAFTING_TABLE_MENU;
    public static RegistryResult<Block> UNCRAFTING_TABLE;

    @Override
    public void init() {
        registry = super.registry;
        INSTANCE = this;

        UNCRAFTING_TABLE = registry.registerBlock(_id("uncraftingtable"), () -> UncraftingTableBlock.UNCRAFTING_TABLE);
        registry.registerItem(_id("uncraftingtable"), () -> ItemUtil.ofBlock(UNCRAFTING_TABLE.getOrNull(), CompatibleItemSettings.of()
                // 1.19.3～
                .addGroup(() -> DefaultItemGroups.FUNCTIONAL, _id("uncraftingtable"))
                // ～1.19.2
                .addGroup(DefaultItemGroups.DECORATIONS)
                )
        );
        UNCRAFTING_TABLE_MENU = registry.registerScreenHandlerType(_id("uncraftingtable"), new SimpleScreenHandlerTypeBuilder<>(UncraftingScreenHandler::new));

        UncraftingScreenHandler.init();

        ServerNetworking.registerReceiver(_id("network"), (e -> {
            NbtCompound nbt = PacketByteUtil.readNbt(e.getBuf());
            if (NbtUtil.has(nbt, "control")) {
                Player player = e.getPlayer();
                int ctrl = NbtUtil.getInt(nbt, "control");
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
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public String getName() {
        return MOD_NAME;
    }

    public static CompatIdentifier _id(String id) {
        return CompatIdentifier.of(MOD_ID, id);
    }
}
