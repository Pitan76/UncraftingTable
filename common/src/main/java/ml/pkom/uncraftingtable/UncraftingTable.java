package ml.pkom.uncraftingtable;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registry.BLOCK_KEY);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_KEY);
    public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(MOD_ID, Registry.MENU_KEY);


    public static final RegistrySupplier<Block> supplierUNCRAFTING_TABLE_BLOCK = BLOCKS.register(id("uncraftingtable"), () -> UncraftingTableBlock.UNCRAFTING_TABLE);
    public static final RegistrySupplier<Item> supplierUNCRAFTING_TABLE_ITEM = ITEMS.register(id("uncraftingtable"), () -> new BlockItem(UncraftingTableBlock.UNCRAFTING_TABLE, new Item.Settings().group(ItemGroup.DECORATIONS)));
    public static final RegistrySupplier<ScreenHandlerType<?>> supplierUNCRAFTING_TABLE_MENU = SCREEN_HANDLERS.register(id("uncraftingtable"), () -> MenuRegistry.of((MenuRegistry.SimpleMenuTypeFactory<ScreenHandler>) (id, inventory) -> new UncraftingScreenHandler(id, inventory)));


    public static void init() {

        BLOCKS.register();
        ITEMS.register();
        SCREEN_HANDLERS.register();

        UncraftingScreenHandler.init();

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, id("network"), ((buf, context) -> {
            NbtCompound nbt = buf.readNbt();
            if (nbt.contains("control")) {
                PlayerEntity player = context.getPlayer();
                int ctrl = nbt.getInt("control");
                if (ctrl == 0) {
                    if (!(player.currentScreenHandler instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.currentScreenHandler;
                    if (screenHandler.getSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.getSlot(0);
                        if (slot.getStack().isEmpty()) return;
                        slot.removeRecipeIndex();
                    }
                }
                if (ctrl == 1) {
                    if (!(player.currentScreenHandler instanceof UncraftingScreenHandler)) return;
                    UncraftingScreenHandler screenHandler = (UncraftingScreenHandler) player.currentScreenHandler;
                    if (screenHandler.getSlot(0) instanceof InsertSlot) {
                        InsertSlot slot = (InsertSlot) screenHandler.getSlot(0);
                        if (slot.getStack().isEmpty()) return;
                        slot.addRecipeIndex();
                    }
                }
            }
        }));
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }
}
