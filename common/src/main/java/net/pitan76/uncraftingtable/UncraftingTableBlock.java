package net.pitan76.uncraftingtable;

import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.text.TextComponent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.ScreenHandlerCreateEvent;
import net.pitan76.mcpitanlib.api.sound.CompatBlockSoundGroup;
import net.pitan76.mcpitanlib.core.serialization.CompatMapCodec;
import net.pitan76.mcpitanlib.core.serialization.codecs.CompatBlockMapCodecUtil;

public class UncraftingTableBlock extends CompatBlock {

    public static final CompatMapCodec<UncraftingTableBlock> CODEC = CompatBlockMapCodecUtil.createCodec(UncraftingTableBlock::new);

    private static final TextComponent TITLE = TextComponent.translatable("container.uncraftingtable76.uncrafting");

    public static UncraftingTableBlock UNCRAFTING_TABLE = new UncraftingTableBlock(
            new BlockSettingsBuilder(UncraftingTable.UNCRAFTING_TABLE_ID).material(CompatibleMaterial.WOOD)
                    .hardness(2.5F).sounds(CompatBlockSoundGroup.WOOD).build()
    );

    public UncraftingTableBlock(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public CompatMapCodec<? extends CompatBlock> getCompatCodec() {
        return CODEC;
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        Player player = e.player;
        if (e.isClient())
            return e.success();

        player.openGuiScreen(e.state.createScreenHandlerFactory(e.world, e.pos));
        return e.consume();
    }

    @Override
    public ScreenHandler createScreenHandler(ScreenHandlerCreateEvent e) {
        return new UncraftingScreenHandler(e.syncId, e.inventory);
    }

    @Override
    public Text getScreenTitle() {
        return TITLE.getText();
    }
}
