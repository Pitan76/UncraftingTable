package net.pitan76.uncraftingtable;

import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.entity.CompatContainerUser;
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
    public void onOpen(CompatContainerUser user) {
        super.onOpen(user);
    }

    @Override
    public void onClose(CompatContainerUser user) {
        if (!ItemStackUtil.isEmpty(insertSlot.callGetStack())) {
            insertSlot.player.offerOrDrop(insertSlot.callGetStack());
        }

        super.onClose(user);
    }

    @Override
    public ItemStack callRemoveStack(int slot, int amount) {
        int before = ItemStackUtil.getCount(super.callGetStack(slot)); // Get the count before removing the stack

        ItemStack stack = superRemoveStack(slot, amount);
        if (slot != 0 || before == amount) return stack;

        insertSlot.updateOutSlot(stack);
        return stack;
    }
}
