package net.pitan76.uncraftingtable;

import net.pitan76.mcpitanlib.api.entity.Player;
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
        if (!insertSlot.callGetStack().isEmpty()) {
            insertSlot.player.offerOrDrop(insertSlot.callGetStack());
        }
        super.onClose(player);
    }
}
