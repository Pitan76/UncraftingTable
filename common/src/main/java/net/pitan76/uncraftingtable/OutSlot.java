package net.pitan76.uncraftingtable;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.EnchantmentUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;

import java.util.Map;

public class OutSlot extends CompatibleSlot {
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
        super.callSetStack(stack);
    }

    @Override
    public ItemStack callTakeStack(int amount) {
        int needXp = Config.config.getIntOrDefault("consume_xp", 0);
        if (needXp != 0 && !insertSlot.player.isCreative()) {
            if (needXp > insertSlot.player.getPlayerEntity().totalExperience) {
                insertSlot.player.sendMessage(TextUtil.translatable("message.uncraftingtable76.not_enough_xp"));
                return ItemStackUtil.empty();
            }
        }
        return super.callTakeStack(amount);
    }

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
        if (!insertSlot.player.getWorld().isClient() && stack.isEmpty() && insertSlot.canGet) {
            int needXp = Config.config.getInt("consume_xp");
            if (needXp != 0 && !insertSlot.player.isCreative()) {
                insertSlot.player.getPlayerEntity().addExperience(-needXp);
            }

            int cursorCount = insertSlot.player.getCursorStack().getCount();
            insertSlot.player.getCursorStack().setCount(0);

            if (Config.config.getBoolean("restore_enchantment_book") && !insertSlot.bookSlot.callGetStack().isEmpty()) {
                ItemStack insertStack = insertSlot.callGetStack();
                if (EnchantmentUtil.hasEnchantment(insertStack)) {
                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, 1);
                    Map<CompatEnchantment, Integer> enchantMap = EnchantmentUtil.getEnchantment(insertStack, insertSlot.player.getWorld());

                    EnchantmentUtil.setEnchantment(book, enchantMap, insertSlot.player.getWorld());
                    insertSlot.player.offerOrDrop(book);
                    insertSlot.bookSlot.callGetStack().decrement(1);
                }

            }

            for (int i = 1; i < 10; ++i) {
                insertSlot.player.offerOrDrop(callGetInventory().getStack(i));
                callGetInventory().setStack(i, ItemStackUtil.empty());
            }
            if (insertSlot.callGetStack().getCount() - insertSlot.latestOutputCount == 0) {
                insertSlot.setStackSuper(ItemStackUtil.empty());
            } else {
                ItemStack insertStack = insertSlot.callGetStack().copy();
                insertStack.setCount(insertStack.getCount() - insertSlot.latestOutputCount);
                insertSlot.callSetStack(insertStack);
            }

            if (cursorCount > 0)
                insertSlot.player.getCursorStack().setCount(cursorCount);
        }
        if (insertSlot.player.getWorld().isClient()) {
            insertSlot.callMarkDirty();
        }
    }
}
