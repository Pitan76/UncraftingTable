package ml.pkom.uncraftingtable;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import ml.pkom.mcpitanlibarch.api.client.SimpleHandledScreen;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UncraftingScreen extends SimpleHandledScreen {

    // temporarily
    int width, height, backgroundWidth, backgroundHeight, x, y;
    ScreenHandler handler;
    // ----

    public static Identifier GUI = UncraftingTable.id("textures/uncrafting_table.png");


    public UncraftingScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        this.height = MinecraftClient.getInstance().getWindow().getScaledHeight();
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        this.handler = handler;
    }

    @Override
    public void resizeOverride(MinecraftClient client, int width, int height) {
        super.resizeOverride(client, width, height);
        this.width = MinecraftClient.getInstance().getWindow().getScaledWidth();
        this.height = MinecraftClient.getInstance().getWindow().getScaledHeight();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    public void initOverride() {
        super.initOverride();
        if (Config.config.getBoolean("restore_enchantment_book")) {
            GUI = UncraftingTable.id("textures/uncrafting_table.png");
        } else {
            GUI = UncraftingTable.id("textures/uncrafting_table_nobook.png");
        }

        this.addDrawableChild_compatibility(new TexturedButtonWidget(x + 31,  y +58, 12, 12, 0, 168, 16, GUI, (buttonWidget) -> {
            // クライアントの反映
            if (handler.getSlot(0) instanceof InsertSlot) {
                InsertSlot slot = (InsertSlot) handler.getSlot(0);
                if (slot.getStack().isEmpty()) return;
                slot.removeRecipeIndex();
            }
            // サーバーに送信
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("control", 0);
            buf.writeNbt(nbt);
            NetworkManager.sendToServer(UncraftingTable.id("network"), buf);
        }));

        this.addDrawableChild_compatibility(new TexturedButtonWidget( x + 45, y + 58, 12, 12, 16, 168, 16, GUI, (buttonWidget) -> {
            // クライアントの反映
            if (handler.getSlot(0) instanceof InsertSlot) {
                InsertSlot slot = (InsertSlot) handler.getSlot(0);
                if (slot.getStack().isEmpty()) return;
                slot.addRecipeIndex();
            }
            // サーバーに送信
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("control", 1);
            buf.writeNbt(nbt);
            NetworkManager.sendToServer(UncraftingTable.id("network"), buf);
        }));
    }

    @Override
    public void drawBackgroundOverride(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        ScreenUtil.setBackground(GUI);
        callDrawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void renderOverride(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.callRenderBackground(matrices);
        super.renderOverride(matrices, mouseX, mouseY, delta);
        this.callDrawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
