package net.pitan76.uncraftingtable;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.ExtendBlock;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.ScreenHandlerCreateEvent;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import org.jetbrains.annotations.Nullable;

public class UncraftingTableBlock extends ExtendBlock {

    private static final Text TITLE = TextUtil.translatable("container.uncraftingtable76.uncrafting");

    public static UncraftingTableBlock UNCRAFTING_TABLE = new UncraftingTableBlock(CompatibleBlockSettings
            .of(CompatibleMaterial.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)
    );

    public UncraftingTableBlock(CompatibleBlockSettings settings) {
        super(settings.build());
    }

    @Override
    public ActionResult onRightClick(BlockUseEvent e) {
        Player player = e.player;
        if (e.isClient())
            return ActionResult.SUCCESS;

        player.openGuiScreen(e.state.createScreenHandlerFactory(e.world, e.pos));
        return ActionResult.CONSUME;
    }

    @Override
    public @Nullable ScreenHandler createScreenHandler(ScreenHandlerCreateEvent e) {
        return new UncraftingScreenHandler(e.syncId, e.inventory);
    }

    @Override
    public @Nullable Text getScreenTitle() {
        return TITLE;
    }
}
