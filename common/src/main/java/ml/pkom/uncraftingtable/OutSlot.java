package ml.pkom.uncraftingtable;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class OutSlot extends Slot {
    // 3 * 3 Slot
    public InsertSlot insertSlot;

    public OutSlot(Inventory inventory, int index, int x, int y, InsertSlot slot) {
        super(inventory, index, x, y);
        this.insertSlot = slot;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public void superSetStack(ItemStack stack) {
        super.setStack(stack);
    }

    @Override
    public ItemStack takeStack(int amount) {
        int needXp = Config.config.getInt("consume_xp");
        if (needXp != 0 && !insertSlot.player.isCreative()) {
            if (needXp > insertSlot.player.totalExperience) {
                insertSlot.player.sendMessage(Utils.translatableText("message.uncraftingtable76.not_enough_xp"), false);
                return ItemStack.EMPTY;
            }
        }
        return super.takeStack(amount);
    }

    @Override
    public void setStack(ItemStack stack) {
        super.setStack(stack);
        if (!insertSlot.player.world.isClient() && stack.isEmpty() && insertSlot.canGet) {
            int needXp = Config.config.getInt("consume_xp");
            if (needXp != 0 && !insertSlot.player.isCreative()) {
                insertSlot.player.addExperience(-needXp);
            }

            //insertSlot.player.inventory.offerOrDrop(insertSlot.player.playerScreenHandler.getCursorStack());
            //insertSlot.player.playerScreenHandler.getCursorStack().setCount(0);

            for (int i = 1;i < 10;i++) {
                insertSlot.player.inventory.offerOrDrop(insertSlot.player.world, inventory.getStack(i));
                inventory.setStack(i, ItemStack.EMPTY);
            }
            if (insertSlot.getStack().getCount() - insertSlot.latestOutputCount == 0) {
                insertSlot.setStackSuper(ItemStack.EMPTY);
            } else {
                ItemStack insertStack = insertSlot.getStack().copy();
                insertStack.setCount(insertStack.getCount() - insertSlot.latestOutputCount);
                insertSlot.setStack(insertStack);
            }
        }
        if (insertSlot.player.world.isClient()) {
            insertSlot.markDirty();
        }
    }
}
