package net.pitan76.uncraftingtable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.EnchantmentUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.inventory.ICompatInventory;

import java.util.Map;

public class OutSlot extends CompatibleSlot {
    // 3 * 3 Slot
    public InsertSlot insertSlot;

    public OutSlot(ICompatInventory inventory, int index, int x, int y, InsertSlot slot) {
        super(inventory, index, x, y);
        this.insertSlot = slot;
    }

    @Override
    public boolean canInsert(net.pitan76.mcpitanlib.midohra.item.ItemStack stack) {
        return false;
    }

    public void superSetStack(ItemStack stack) {
        super.callSetStack(stack);
    }

    @Override
    public ItemStack callTakeStack(int amount) {
        int needXp = Config.config.getIntOrDefault("consume_xp", 0);
        Player player = insertSlot.player;

        if (needXp != 0 && !player.isCreative()) {
            if (needXp > player.getTotalExperience()) {
                player.sendMessage(TextUtil.translatable("message.uncraftingtable76.not_enough_xp"));
                return ItemStackUtil.empty();
            }
        }
        return super.callTakeStack(amount);
    }

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
        Player player = insertSlot.player;

        if (!player.isClient() && ItemStackUtil.isEmpty(stack) && insertSlot.canGet) {
            int needXp = Config.config.getInt("consume_xp");
            if (needXp != 0 && !player.isCreative()) {
                player.addExperience(-needXp);
            }

            int cursorCount = ItemStackUtil.getCount(player.getCursorStack());
            ItemStackUtil.setCount(player.getCursorStack(), 0);

            if (Config.config.getBooleanOrDefault("restore_enchantment_book", true) && !insertSlot.bookSlot.callGetStack().isEmpty()) {
                ItemStack insertStack = insertSlot.callGetStack();
                if (EnchantmentUtil.hasEnchantment(insertStack)) {
                    ItemStack book = ItemStackUtil.create(Items.ENCHANTED_BOOK, 1);
                    Map<CompatEnchantment, Integer> enchantMap = EnchantmentUtil.getEnchantment(insertStack, player.getWorld());

                    EnchantmentUtil.setEnchantment(book, enchantMap, player.getWorld());
                    player.offerOrDrop(book);
                    ItemStackUtil.decrementCount(insertSlot.bookSlot.callGetStack(), 1);
                }

            }

            for (int i = 1; i < 10; ++i) {
                player.offerOrDrop(callGetInventory().getStack(i));
                callGetInventory().setStack(i, ItemStackUtil.empty());
            }
            if (ItemStackUtil.getCount(insertSlot.callGetStack()) - insertSlot.latestOutputCount == 0) {
                insertSlot.setStackSuper(ItemStackUtil.empty());
            } else {
                ItemStack insertStack = ItemStackUtil.copy(insertSlot.callGetStack());
                insertStack.setCount(ItemStackUtil.getCount(insertStack) - insertSlot.latestOutputCount);
                insertSlot.callSetStack(insertStack);
            }

            if (cursorCount > 0)
                ItemStackUtil.setCount(player.getCursorStack(), cursorCount);
        }
        if (player.isClient()) {
            insertSlot.callMarkDirty();
        }
    }
}
