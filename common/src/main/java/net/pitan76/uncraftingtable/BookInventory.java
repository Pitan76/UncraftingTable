package net.pitan76.uncraftingtable;

import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.util.inventory.CompatInventory;

public class BookInventory extends CompatInventory {
    public BookInventory() {
        super(1);
    }

    public BookSlot bookSlot;

    public void setBookSlot(BookSlot bookSlot) {
        this.bookSlot = bookSlot;
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
    }

    @Override
    public void onClose(Player player) {
        if (bookSlot != null && !bookSlot.callGetStack().isEmpty()) {
            bookSlot.player.offerOrDrop(bookSlot.callGetStack());
        }

        super.onClose(player);
    }
}
