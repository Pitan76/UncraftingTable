package net.pitan76.uncraftingtable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class UncraftingScreenHandler extends SimpleScreenHandler {

    private final UncraftingInventory uncraftingInventory;
    public final BookInventory bookInventory;

    public UncraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(UncraftingTable.UNCRAFTING_TABLE_MENU.get(), syncId);
        uncraftingInventory = new UncraftingInventory();
        bookInventory = new BookInventory();
        int m, l;
        InsertSlot insertSlot = new InsertSlot(uncraftingInventory, 0, 36, 35, playerInventory.player);
        uncraftingInventory.setInsertSlot(insertSlot);
        callAddSlot(insertSlot);

        // Out Slot
        int i = 0;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 3; ++l) {
                i++;
                callAddSlot(new OutSlot(uncraftingInventory, i, 94 + l * 18, 17 + m * 18, insertSlot));
            }
        }

        // Book Slot
        if (Config.config.getBooleanOrDefault("restore_enchantment_book", true)) {
            BookSlot bookSlot = new BookSlot(bookInventory, 0, 8, 35, new Player(playerInventory.player));
            bookInventory.setBookSlot(bookSlot);
            insertSlot.bookSlot = bookSlot;
            callAddSlot(bookSlot);
        }

        // Player Inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                callAddSlot(new CompatibleSlot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            callAddSlot(new CompatibleSlot(playerInventory, m, 8 + m * 18, 142));
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
        Slot slot = ScreenHandlerUtil.getSlot(this, slotIndex);
        if (!(slot instanceof OutSlot)) {
            super.overrideOnSlotClick(slotIndex, button, actionType, player);
            return;
        }

        quickMoveOverride(player, slotIndex);
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        ItemStack newStack = ItemStackUtil.empty();
        Slot slot = ScreenHandlerUtil.getSlot(this, index);

        if (SlotUtil.hasStack(slot)) {
            // 経験値の確認
            if (slot instanceof OutSlot) {
                int needXp = Config.config.getIntOrDefault("consume_xp", 0);
                if (needXp != 0 && !player.isCreative()) {
                    if (needXp > player.getPlayerEntity().totalExperience) {
                        player.sendMessage(TextUtil.translatable("message.uncraftingtable76.not_enough_xp"));
                        return ItemStackUtil.empty();
                    }
                }
            }

            ItemStack originalStack = SlotUtil.getStack(slot);
            newStack = originalStack.copy();

            // Uncrafting Inventory のサイズよりも小さい場合は Uncrafting Inventory内のスロットである
            if (index < this.uncraftingInventory.size()) {
                // InsertSlot, OutSlot -> Player Inventory
                if (!this.callInsertItem(originalStack, this.uncraftingInventory.size(), ScreenHandlerUtil.getSlots(this).size(), true)) {
                    return ItemStackUtil.empty();
                }
            } else if (!this.callInsertItem(originalStack, 0, 1, false)) {
                return ItemStackUtil.empty();
            } else {
                // Player Inventory → InsertSlot, OutSlot
                uncraftingInventory.insertSlot.updateOutSlot(uncraftingInventory.insertSlot.callGetStack());
            }

            if (originalStack.isEmpty()) {
                SlotUtil.setStack(slot, ItemStackUtil.empty());
            } else {
                SlotUtil.markDirty(slot);
            }

        }
        return newStack;
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        if (slot instanceof OutSlot)
            return false;

        return super.canInsertIntoSlot(slot);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.canInsert(stack);
    }

    @Override
    public void close(Player player) {
        PlayerEntity playerEntity = player.getPlayerEntity();
        uncraftingInventory.onClose(playerEntity);
        bookInventory.onClose(playerEntity);
        super.close(player);
    }
}
