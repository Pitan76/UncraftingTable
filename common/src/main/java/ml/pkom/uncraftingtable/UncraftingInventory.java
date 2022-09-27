package ml.pkom.uncraftingtable;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;

//import org.apache.logging.log4j.Level;

public class UncraftingInventory extends SimpleInventory {
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
        //UncraftingTable.log(Level.INFO, insertSlot.getStack().getName().getString());
        if (!insertSlot.getStack().isEmpty()) {
            insertSlot.player.getInventory().offerOrDrop(insertSlot.getStack());
            /*
            if (OutSlot.canGive(insertSlot.player.inventory.main)) {
                insertSlot.player.inventory.offerOrDrop(player.world, insertSlot.getStack());
            } else {
                insertSlot.player.dropItem(insertSlot.getStack(), false);
            }
             */
        }
        super.onClose(player.getPlayerEntity());
    }
}
