package net.pitan76.uncraftingtable;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.ExtendBlock;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.ScreenHandlerCreateEvent;
import net.pitan76.mcpitanlib.api.sound.CompatBlockSoundGroup;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.core.serialization.CompatMapCodec;

public class UncraftingTableBlock extends ExtendBlock {

    public static final CompatMapCodec<UncraftingTableBlock> CODEC = CompatMapCodec.createCodecOfExtendBlock(
            UncraftingTableBlock::new);

    @Override
    public CompatMapCodec<? extends UncraftingTableBlock> getCompatCodec() {
        return CODEC;
    }

    private static final Text TITLE = TextUtil.translatable("container.uncraftingtable76.uncrafting");

    public static UncraftingTableBlock UNCRAFTING_TABLE = new UncraftingTableBlock(CompatibleBlockSettings
            .of(CompatibleMaterial.WOOD).strength(2.5F).sounds(CompatBlockSoundGroup.WOOD)
    );

    public UncraftingTableBlock(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onRightClick(BlockUseEvent e) {
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
        return TITLE;
    }
}
