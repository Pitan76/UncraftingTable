package ml.pkom.uncraftingtable;

import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UncraftingTableBlock extends CraftingTableBlock {

    private static final Text TITLE = TextUtil.translatable("container.uncraftingtable76.uncrafting");

    public static UncraftingTableBlock UNCRAFTING_TABLE = new UncraftingTableBlock(CompatibleBlockSettings
            .of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)
    );

    public UncraftingTableBlock(CompatibleBlockSettings settings) {
        super(settings.build());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new UncraftingScreenHandler(i, playerInventory), TITLE);
    }
}
