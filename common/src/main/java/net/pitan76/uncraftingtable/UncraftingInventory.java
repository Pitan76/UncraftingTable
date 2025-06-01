package net.pitan76.uncraftingtable;

import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.inventory.CompatInventory;

public class UncraftingInventory extends CompatInventory {
    public UncraftingInventory() {
        super(10);
    }

    public InsertSlot insertSlot;

    public void setInsertSlot(InsertSlot insertSlot) {
        this.insertSlot = insertSlot;
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
    }

    @Override
    public void onClose(Player player) {
        if (!ItemStackUtil.isEmpty(insertSlot.callGetStack())) {
            insertSlot.player.offerOrDrop(insertSlot.callGetStack());
        }
        super.onClose(player);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        int before = ItemStackUtil.getCount(super.callGetStack(slot)); // Get the count before removing the stack

        ItemStack stack = super.removeStack(slot, amount);
        if (slot != 0 || before == amount) return stack;

        insertSlot.updateOutSlot(stack);
        return stack;
    }
}
