package ml.pkom.uncraftingtable;

import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

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
            if (needXp > insertSlot.player.getPlayerEntity().totalExperience) {
                insertSlot.player.getPlayerEntity().sendMessage(TextUtil.translatable("message.uncraftingtable76.not_enough_xp"), false);
                return ItemStack.EMPTY;
            }
        }
        return super.takeStack(amount);
    }

    @Override
    public void setStack(ItemStack stack) {
        super.setStack(stack);
        if (!insertSlot.player.getWorld().isClient() && stack.isEmpty() && insertSlot.canGet) {
            int needXp = Config.config.getInt("consume_xp");
            if (needXp != 0 && !insertSlot.player.isCreative()) {
                insertSlot.player.getPlayerEntity().addExperience(-needXp);
            }

            insertSlot.player.offerOrDrop(insertSlot.player.getCursorStack());
            insertSlot.player.getCursorStack().setCount(0);

            if (Config.config.getBoolean("restore_enchantment_book") && !insertSlot.bookSlot.getStack().isEmpty()) {
                ItemStack insertStack = insertSlot.getStack();
                if (insertStack.hasEnchantments()) {
                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, 1);
                    Map<Enchantment, Integer> enchantMap = EnchantmentHelper.get(insertStack);
                    //for (Map.Entry<Enchantment, Integer> entry : enchantMap.entrySet()) {
                    //    book.addEnchantment(entry.getKey(), entry.getValue());
                    //}
                    EnchantmentHelper.set(enchantMap, book);
                    insertSlot.player.offerOrDrop(book);
                    insertSlot.bookSlot.getStack().decrement(1);
                }

            }

            for (int i = 1;i < 10;i++) {
                insertSlot.player.offerOrDrop(inventory.getStack(i));
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
        if (insertSlot.player.getWorld().isClient()) {
            insertSlot.markDirty();
        }
    }
}
