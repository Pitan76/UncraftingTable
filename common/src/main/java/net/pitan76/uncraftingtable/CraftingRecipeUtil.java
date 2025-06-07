package net.pitan76.uncraftingtable;

import net.pitan76.mcpitanlib.api.util.recipe.RecipeUtil;
import net.pitan76.mcpitanlib.midohra.recipe.CraftingRecipe;
import net.pitan76.mcpitanlib.midohra.recipe.ShapedRecipe;
import net.pitan76.mcpitanlib.midohra.recipe.ShapelessRecipe;
import net.pitan76.mcpitanlib.midohra.recipe.entry.RecipeEntry;
import net.pitan76.mcpitanlib.midohra.recipe.input.CraftingRecipeInputOrInventory;
import net.pitan76.mcpitanlib.midohra.world.ServerWorld;
import net.pitan76.mcpitanlib.midohra.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class CraftingRecipeUtil {
    @Nullable
    public static net.minecraft.item.ItemStack getOutput(CraftingRecipe recipe, CraftingRecipeInputOrInventory input, net.minecraft.world.World world) {
        if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
            return recipe.getOutput(input, world);
        } else {
            try {
                return recipe.getOutput(input, world);
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

    public static Collection<CraftingRecipe> getCraftingRecipes(ServerWorld world) {
        try {
            return RecipeUtil.getCraftingRecipes(world).stream().map(
                    recipe -> {
                        if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe)
                            return recipe;

                        if (recipe.getRaw() instanceof net.minecraft.recipe.ShapedRecipe)
                            return ShapedRecipe.of((net.minecraft.recipe.ShapedRecipe) recipe.getRaw());

                        if (recipe.getRaw() instanceof net.minecraft.recipe.ShapelessRecipe)
                            return ShapelessRecipe.of((net.minecraft.recipe.ShapelessRecipe) recipe.getRaw());

                        return recipe;
                    }
            ).collect(Collectors.toList());
        } catch (Exception e) {
            return RecipeUtil.getCraftingRecipes(world);
        }
    }
}
