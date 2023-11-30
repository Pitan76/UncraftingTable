package ml.pkom.uncraftingtable;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.gui.SimpleScreenHandler;
import ml.pkom.mcpitanlibarch.api.util.ScreenHandlerUtil;
import ml.pkom.mcpitanlibarch.api.util.SlotUtil;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class UncraftingScreenHandler extends SimpleScreenHandler {

    private final UncraftingInventory inventory;
    public final BookInventory bookInventory;

    public UncraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(UncraftingTable.supplierUNCRAFTING_TABLE_MENU.getOrNull(), syncId);
        inventory = new UncraftingInventory();
        bookInventory = new BookInventory();
        int m, l;
        InsertSlot insertSlot = new InsertSlot(inventory, 0, 36, 35, playerInventory.player);
        inventory.setInsertSlot(insertSlot);
        callAddSlot(insertSlot);

        // Out Slot
        int i = 0;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 3; ++l) {
                i++;
                callAddSlot(new OutSlot(inventory, i, 94 + l * 18, 17 + m * 18, insertSlot));
            }
        }

        // Book Slot
        if (Config.config.getBoolean("restore_enchantment_book")) {
            BookSlot bookSlot = new BookSlot(bookInventory, 0, 8, 35, new Player(playerInventory.player));
            bookInventory.setBookSlot(bookSlot);
            insertSlot.bookSlot = bookSlot;
            callAddSlot(bookSlot);
        }

        // Player Inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                callAddSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            callAddSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    public static void init() {

    }

    @Override
    public void overrideOnSlotClick(int slotIndex, int button, SlotActionType actionType, Player player) {
        if (actionType != SlotActionType.PICKUP || ScreenHandlerUtil.getSlots(this).size() <= slotIndex || slotIndex < 0) {
            super.overrideOnSlotClick(slotIndex, button, actionType, player);
            return;
        }
        Slot slot = ScreenHandlerUtil.getSlots(this).get(slotIndex);
        if (!(slot instanceof OutSlot)) {
            super.overrideOnSlotClick(slotIndex, button, actionType, player);
            return;
        }

        quickMoveOverride(player, slotIndex);
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = ScreenHandlerUtil.getSlots(this).get(invSlot);
        if (slot.hasStack()) {
            // 経験値の確認
            if (slot instanceof OutSlot) {
                int needXp = Config.config.getInt("consume_xp");
                if (needXp != 0 && !player.isCreative()) {
                    if (needXp > player.getPlayerEntity().totalExperience) {
                        player.getPlayerEntity().sendMessage(TextUtil.translatable("message.uncraftingtable76.not_enough_xp"), false);
                        return ItemStack.EMPTY;
                    }
                }
            }

            ItemStack originalStack = SlotUtil.getStack(slot);
            newStack = originalStack.copy();

            // Uncrafting Inventory
            if (invSlot < this.inventory.size()) {
                if (!this.callInsertItem(originalStack, this.inventory.size(), ScreenHandlerUtil.getSlots(this).size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.callInsertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            } else {
                // Player Inventory → つまり、InsertSlotへ入れている可能性が高い
                inventory.insertSlot.updateOutSlot(inventory.insertSlot.callGetStack());
            }

            if (originalStack.isEmpty()) {
                SlotUtil.setStack(slot, ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public void close(Player player) {
        PlayerEntity playerEntity = player.getPlayerEntity();
        inventory.onClose(playerEntity);
        bookInventory.onClose(playerEntity);
        super.close(player);
    }
}
