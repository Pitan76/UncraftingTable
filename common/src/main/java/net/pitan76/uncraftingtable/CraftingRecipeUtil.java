package net.pitan76.uncraftingtable;

import net.pitan76.mcpitanlib.api.util.recipe.RecipeUtil;
import net.pitan76.mcpitanlib.midohra.recipe.CraftingRecipe;
import net.pitan76.mcpitanlib.midohra.recipe.ShapedRecipe;
import net.pitan76.mcpitanlib.midohra.recipe.ShapelessRecipe;
import net.pitan76.mcpitanlib.midohra.recipe.entry.RecipeEntry;
import net.pitan76.mcpitanlib.midohra.recipe.input.CraftingRecipeInputOrInventory;
import net.pitan76.mcpitanlib.midohra.world.World;
import org.jetbrains.annotations.Nullable;

public class CraftingRecipeUtil {
    @Nullable
    public static net.minecraft.item.ItemStack getOutput(CraftingRecipe recipe, CraftingRecipeInputOrInventory input, net.minecraft.world.World world) {
        if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
            return recipe.getOutput(input, world);
        } else {
            try {
                return recipe.craft(input, world);
            } catch (ArrayIndexOutOfBoundsException e) {
                String id = "Unknown recipe";
                for (RecipeEntry entry : RecipeUtil.getRecipeEntries(World.of(world))) {
                    if (entry.getRawRecipe() == recipe.getRaw()) {
                        id = entry.getId().toString();
                        break;
                    }
                }

                UncraftingTable.INSTANCE.logger.error(e.getMessage() + ": " + id);
                return null;
            } catch (Exception ignored) {
                return null;
            }
        }
    }
}
