package net.pitan76.uncraftingtable.client;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.client.gui.screen.CompatInventoryScreen;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.InsertSlot;
import net.pitan76.uncraftingtable.UncraftingScreenHandler;
import net.pitan76.uncraftingtable.UncraftingTable;

public class UncraftingScreen extends CompatInventoryScreen<UncraftingScreenHandler> {

    private final UncraftingScreenHandler handler;

    public static CompatIdentifier gui = UncraftingTable._id("textures/gui/uncrafting_table.png");

    public UncraftingScreen(UncraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setBackgroundWidth(176);
        setBackgroundHeight(166);

        this.handler = handler;
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return gui;
    }

    @Override
    public void initOverride() {
        super.initOverride();
        gui = UncraftingTable._id(Config.config.getBooleanOrDefault("restore_enchantment_book", true) ?
                "textures/gui/uncrafting_table.png" : "textures/gui/uncrafting_table_nobook.png");

        this.addDrawableCTBW(ScreenUtil.createTexturedButtonWidget(x + 31,  y + 58, 12, 12, 0, 168, 16, gui, (buttonWidget) -> {
            // クライアントの反映
            if (handler.callGetSlot(0) instanceof InsertSlot) {
                InsertSlot slot = (InsertSlot) handler.callGetSlot(0);
                if (slot.callGetStack().isEmpty()) return;
                slot.prevRecipeIndex();
            }

            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            PacketByteUtil.writeInt(buf, 0);
            ClientNetworking.send(UncraftingTable._id("network_ctrl"), buf);
        }));

        this.addDrawableCTBW(ScreenUtil.createTexturedButtonWidget( x + 45, y + 58, 12, 12, 16, 168, 16, gui, (buttonWidget) -> {
            // クライアントの反映
            if (handler.callGetSlot(0) instanceof InsertSlot) {
                InsertSlot slot = (InsertSlot) handler.callGetSlot(0);
                if (slot.callGetStack().isEmpty()) return;
                slot.nextRecipeIndex();
            }

            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            PacketByteUtil.writeInt(buf, 1);
            ClientNetworking.send(UncraftingTable._id("network_ctrl"), buf);
        }));

    }
}
