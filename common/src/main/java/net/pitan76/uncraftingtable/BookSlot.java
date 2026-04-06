package net.pitan76.uncraftingtable;

import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.inventory.ICompatInventory;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import net.pitan76.mcpitanlib.midohra.item.MCItems;

public class BookSlot extends CompatibleSlot {
    public Player player;

    public BookSlot(ICompatInventory inventory, int index, int x, int y, Player player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    public boolean canInsert(ItemStack stack) {
        return ItemUtil.isEqual(ItemStackUtil.getItem(stack), MCItems.BOOK.get());
    }
}
