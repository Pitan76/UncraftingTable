package net.pitan76.uncraftingtable;

import net.minecraft.entity.player.PlayerEntity;
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

    public void onOpen(PlayerEntity player) {
        super.onOpen(player);
    }

    public void onClose(PlayerEntity playerEntity) {
        Player player = new Player(playerEntity);
        if (!insertSlot.callGetStack().isEmpty()) {
            insertSlot.player.offerOrDrop(insertSlot.callGetStack());
        }
        super.onClose(player.getPlayerEntity());
    }
}
