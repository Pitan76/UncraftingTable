package net.pitan76.uncraftingtable.client;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.client.SimpleHandledScreen;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawBackgroundArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawMouseoverTooltipArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.RenderArgs;
import net.pitan76.mcpitanlib.api.network.ClientNetworking;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.InsertSlot;
import net.pitan76.uncraftingtable.UncraftingScreenHandler;
import net.pitan76.uncraftingtable.UncraftingTable;

public class UncraftingScreen extends SimpleHandledScreen {

    private final UncraftingScreenHandler handler;

    public static CompatIdentifier GUI = UncraftingTable.id("textures/gui/uncrafting_table.png");

    public UncraftingScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setBackgroundWidth(176);
        setBackgroundHeight(166);

        this.handler = (UncraftingScreenHandler) handler;
    }

    @Override
    public void initOverride() {
        super.initOverride();
        GUI = Config.config.getBoolean("restore_enchantment_book") ?
                UncraftingTable.id("textures/gui/uncrafting_table.png") : UncraftingTable.id("textures/gui/uncrafting_table_nobook.png");

        this.addDrawableCTBW(ScreenUtil.createTexturedButtonWidget(x + 31,  y + 58, 12, 12, 0, 168, 16, GUI.toMinecraft(), (buttonWidget) -> {
            // クライアントの反映
            if (handler.callGetSlot(0) instanceof InsertSlot) {
                InsertSlot slot = (InsertSlot) handler.callGetSlot(0);
                if (slot.callGetStack().isEmpty()) return;
                slot.prevRecipeIndex();
            }

            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("control", 0);
            PacketByteUtil.writeNbt(buf, nbt);
            ClientNetworking.send(UncraftingTable.id("network").toMinecraft(), buf);
        }));

        this.addDrawableCTBW(ScreenUtil.createTexturedButtonWidget( x + 45, y + 58, 12, 12, 16, 168, 16, GUI.toMinecraft(), (buttonWidget) -> {
            // クライアントの反映
            if (handler.callGetSlot(0) instanceof InsertSlot) {
                InsertSlot slot = (InsertSlot) handler.callGetSlot(0);
                if (slot.callGetStack().isEmpty()) return;
                slot.nextRecipeIndex();
            }

            // サーバーに送信
            PacketByteBuf buf = PacketByteUtil.create();
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("control", 1);
            PacketByteUtil.writeNbt(buf, nbt);
            ClientNetworking.send(UncraftingTable.id("network").toMinecraft(), buf);
        }));

    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        callDrawTexture(args.drawObjectDM, GUI.toMinecraft(), x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void renderOverride(RenderArgs args) {
        this.callRenderBackground(args);
        super.renderOverride(args);
        this.callDrawMouseoverTooltip(new DrawMouseoverTooltipArgs(args.drawObjectDM, args.mouseX, args.mouseY));
    }
}
