package net.pitan76.uncraftingtable;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;

public class BookInventory extends SimpleInventory {
    public BookInventory() {
        super(1);
    }

    public BookSlot bookSlot;

    public void setBookSlot(BookSlot bookSlot) {
        this.bookSlot = bookSlot;
    }

    public void onOpen(PlayerEntity player) {
        super.onOpen(player);
    }

    public void onClose(PlayerEntity playerEntity) {
        Player player = new Player(playerEntity);
        if (bookSlot != null)
            if (!bookSlot.callGetStack().isEmpty()) {
                bookSlot.player.offerOrDrop(bookSlot.callGetStack());
            }

        super.onClose(player.getPlayerEntity());
    }
}
