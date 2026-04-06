package net.pitan76.uncraftingtable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.gui.args.SlotClickEvent;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.inventory.CompatPlayerInventory;

public class UncraftingScreenHandler extends SimpleScreenHandler {

    private final UncraftingInventory uncraftingInventory;
    public final BookInventory bookInventory;

    public boolean hasBookSlot = true;

    public UncraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, new CompatPlayerInventory(playerInventory));
    }

    public UncraftingScreenHandler(int syncId, CompatPlayerInventory playerInventory) {
        super(UncraftingTable.UNCRAFTING_TABLE_MENU.get(), syncId);
        uncraftingInventory = new UncraftingInventory();
        bookInventory = new BookInventory();

        Player player = playerInventory.getPlayer();

        int y, x;
        InsertSlot insertSlot = new InsertSlot(uncraftingInventory, 0, 36, 35, player);
        uncraftingInventory.setInsertSlot(insertSlot);
        callAddSlot(insertSlot);

        // Out Slot
        int i = 0;
        for (y = 0; y < 3; ++y) {
            for (x = 0; x < 3; ++x) {
                i++;
                callAddSlot(new OutSlot(uncraftingInventory, i, 94 + x * 18, 17 + y * 18, insertSlot));
            }
        }

        hasBookSlot = Config.config.getBooleanOrDefault("restore_enchantment_book", true);

        // Book Slot
        if (hasBookSlot) {
            BookSlot bookSlot = new BookSlot(bookInventory, 0, 8, 35, player);
            bookInventory.setBookSlot(bookSlot);
            insertSlot.bookSlot = bookSlot;
            callAddSlot(bookSlot);
        }

        // Player Inventory
        for (y = 0; y < 3; ++y) {
            for (x = 0; x < 9; ++x) {
                callAddSlot(new CompatibleSlot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        for (y = 0; y < 9; ++y) {
            callAddSlot(new CompatibleSlot(playerInventory, y, 8 + y * 18, 142));
        }
    }

    public static void init() {

    }

    @Override
    public void onSlotClick(SlotClickEvent e) {
        int slotIndex = e.getSlot();

        if (!e.isPickupAction() || ScreenHandlerUtil.getSlots(this).size() <= slotIndex || slotIndex < 0) {
            super.onSlotClick(e);
            return;
        }

        Slot slot = ScreenHandlerUtil.getSlot(this, slotIndex);
        if (!(slot instanceof OutSlot)) {
            super.onSlotClick(e);
            return;
        }

        quickMoveOverride(e.getPlayer(), slotIndex);
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
                    if (needXp > player.getTotalExperience()) {
                        player.sendMessage(TextUtil.translatable("message.uncraftingtable76.not_enough_xp"));
                        return ItemStackUtil.empty();
                    }
                }
            }

            ItemStack originalStack = SlotUtil.getStack(slot);
            newStack = ItemStackUtil.copy(originalStack);

            // Uncrafting Inventory のサイズよりも小さい場合は Uncrafting Inventory内のスロットである
            if (index < this.uncraftingInventory.getSize()) {
                // InsertSlot, OutSlot -> Player Inventory
                if (!this.callInsertItem(originalStack, this.uncraftingInventory.getSize(), ScreenHandlerUtil.getSlots(this).size(), true)) {
                    return ItemStackUtil.empty();
                }
            } else if (!this.callInsertItem(originalStack, 0, 1, false)) {
                return ItemStackUtil.empty();
            } else {
                // Player Inventory → InsertSlot, OutSlot
                uncraftingInventory.insertSlot.updateOutSlot(uncraftingInventory.insertSlot.callGetStack());
            }

            if (ItemStackUtil.isEmpty(originalStack)) {
                SlotUtil.setStack(slot, ItemStackUtil.empty());
            } else {
                SlotUtil.markDirty(slot);
            }
        }

        return newStack;
    }

    @Override
    public boolean canInsertIntoSlotOverride(Slot slot) {
        return !(slot instanceof OutSlot) && super.canInsertIntoSlotOverride(slot);
    }

    @Override
    public boolean canInsertIntoSlotOverride(net.pitan76.mcpitanlib.midohra.item.ItemStack stack, Slot slot) {
        return slot.canInsert(stack.toMinecraft());
    }

    @Override
    public void close(Player player) {
        uncraftingInventory.onClose(player);
        bookInventory.onClose(player);
        super.close(player);
    }
}
