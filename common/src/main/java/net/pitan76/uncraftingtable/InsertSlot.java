package net.pitan76.uncraftingtable;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import net.pitan76.mcpitanlib.api.util.recipe.RecipeMatcherUtil;
import net.pitan76.mcpitanlib.midohra.recipe.*;
import net.pitan76.mcpitanlib.midohra.recipe.input.CraftingRecipeInputOrInventory;
import net.pitan76.mcpitanlib.midohra.world.ServerWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InsertSlot extends CompatibleSlot {
    public Player player;
    public int recipeIndex = 0;

    // アイテムのコモンタグ(鉱石辞書)のインデックス (未開発)
    public int tagItemIndex = 0;

    public List<Recipe> latestOutRecipes = new ArrayList<>();
    public ItemStack latestItemStack = ItemStackUtil.empty();

    // OutSlotでGetできるかどうか。(バグ対策)
    public boolean canGet = true;

    public BookSlot bookSlot;

    public InsertSlot(Inventory inventory, int index, int x, int y, Player player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    public int getMaxTagItemIndex() {
        if (latestOutRecipes.isEmpty()) return 0;
        if (latestItemStack.isEmpty()) return 0;

        int max = 0;
        for (net.minecraft.recipe.Ingredient rawIngredient : latestOutRecipes.get(recipeIndex).getInputs()) {
            Ingredient ingredient = Ingredient.of(rawIngredient);

            int size = ingredient.getMatchingStacksIds().size();
            if (size - 1 > max) {
                max = size - 1;
            }
        }
        return max;
    }

    public void nextRecipeIndex() {
        if (latestOutRecipes.isEmpty()) return;
        if (latestItemStack.isEmpty()) return;

        int maxTagItemIndex = getMaxTagItemIndex();
        if (maxTagItemIndex == 0) {
            tagItemIndex = 0;
            recipeIndex++;
            int maxIndex = latestOutRecipes.size() - 1;
            if (recipeIndex > maxIndex) {
                recipeIndex = 0;
            }
            latestItemStack.setCount(callGetStack().getCount());
            callSetStack(latestItemStack);
            return;
        }

        tagItemIndex++;
        if (tagItemIndex > maxTagItemIndex) {
            tagItemIndex = 0;
            recipeIndex++;
            int maxIndex = latestOutRecipes.size() - 1;
            if (recipeIndex > maxIndex) {
                recipeIndex = 0;
            }
        }
        latestItemStack.setCount(callGetStack().getCount());
        callSetStack(latestItemStack);
    }

    public void prevRecipeIndex() {
        if (latestOutRecipes.isEmpty()) return;
        if (latestItemStack.isEmpty()) return;

        int maxTagItemIndex = getMaxTagItemIndex();
        if (maxTagItemIndex == 0) {
            tagItemIndex = 0;
            recipeIndex--;
            int maxIndex = latestOutRecipes.size() - 1;
            if (recipeIndex < 0) {
                recipeIndex = maxIndex;
            }
            latestItemStack.setCount(callGetStack().getCount());
            callSetStack(latestItemStack);
            return;
        }

        tagItemIndex--;
        if (tagItemIndex < 0) {
            tagItemIndex = maxTagItemIndex;
            recipeIndex--;
            int maxIndex = latestOutRecipes.size() - 1;
            if (recipeIndex < 0) {
                recipeIndex = maxIndex;
            }
        }
        latestItemStack.setCount(callGetStack().getCount());
        callSetStack(latestItemStack);
    }

    public static boolean ingredientsContains(Collection<Ingredient> ingredients, Item item) {
        for (Ingredient ingredient : ingredients) {
            for (Item matchingItem : ingredient.getMatchingItems()) {
                if (matchingItem.equals(item))
                    return true;
            }
        }
        return false;
    }

    public void updateOutSlot(ItemStack stack) {
        if (player.isClient()) return;

        for (int i = 1; i < 10; ++i)
            ((OutSlot)((UncraftingScreenHandler) player.getCurrentScreenHandler()).callGetSlot(i)).superSetStack(ItemStackUtil.empty());
        if (stack.isEmpty()) return;
        if (!Config.config.getBooleanOrDefault("uncraft_damaged_item", true)) {
            int damage = ItemStackUtil.getDamage(stack);
            if (damage != 0 && damage != ItemStackUtil.getMaxDamage(stack)) {
                return;
            }
        }
        if (player.getWorld() == null) return;
        if (!latestItemStack.getItem().equals(stack.getItem()) && !latestItemStack.isEmpty()) {
            recipeIndex = 0;
            tagItemIndex = 0;
        }
        ServerWorld world = ServerWorld.of((net.minecraft.server.world.ServerWorld) player.getWorld());
        Collection<Recipe> recipes = world.getRecipeManager().getNormalRecipes();
        List<Recipe> outRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ItemStack outputStack;
            if (recipe instanceof ShapedRecipe) {
                outputStack = ((ShapedRecipe) recipe).craft(player.getWorld());
            } else if (recipe instanceof ShapelessRecipe) {
                outputStack = ((ShapelessRecipe) recipe).craft(player.getWorld());
            } else if (recipe instanceof CraftingRecipe) {
                outputStack = ((CraftingRecipe) recipe).craft(CraftingRecipeInputOrInventory.EMPTY, player.getWorld());
            } else {
                continue;
            }

            if (outputStack.getCount() > stack.getCount()) continue;
            // Tech Reborn Disable UU Matter
            if (ItemUtil.isExist("techreborn:uu_matter") && Config.config.getBooleanOrDefault("disable_uncrafting_uu_matter", false) && ingredientsContains(to(recipe.getInputs()), ItemUtil.fromId("techreborn:uu_matter"))) continue;

            if (outputStack.getItem().equals(stack.getItem())) {
                outRecipes.add(recipe);
            }
        }

        if (outRecipes.size() != latestOutRecipes.size()) {
            recipeIndex = 0;
            tagItemIndex = 0;
        }

        latestOutRecipes = outRecipes;
        if (outRecipes.isEmpty() || recipeIndex > outRecipes.size() - 1) return;
        CraftingRecipe recipe = (CraftingRecipe) outRecipes.get(recipeIndex);
        latestOutputCount = recipe.craft(CraftingRecipeInputOrInventory.EMPTY, player.getWorld()).getCount();
        if (!stack.isEmpty())
            latestItemStack = stack.copy();

        List<Ingredient> ingredients = prettyRecipe(recipe);
        setOutStack(0, tagItemIndex, ingredients, 1);
        setOutStack(1, tagItemIndex, ingredients, 1);
        setOutStack(2, tagItemIndex, ingredients, 1);
        setOutStack(3, tagItemIndex, ingredients, 1);
        setOutStack(4, tagItemIndex, ingredients, 1);
        setOutStack(5, tagItemIndex, ingredients, 1);
        setOutStack(6, tagItemIndex, ingredients, 1);
        setOutStack(7, tagItemIndex, ingredients, 1);
        setOutStack(8, tagItemIndex, ingredients, 1);
    }

    /**
     * return list of Ingredients from Recipe in 3x3 format
     * レシピを3x3の形に整形してIngredientのリストを返す
     * @param recipe Recipe
     * @return List<Ingredient> prettied list
     */
    public List<Ingredient> prettyRecipe(Recipe recipe) {
        List<Ingredient> result = new ArrayList<>();
        if (!(recipe instanceof ShapedRecipe)) return to(recipe.getInputs());
        ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
        int width = shapedRecipe.getWidth();

        int empty = 0;
        for (int i = 0; i < 9; i++) {
            List<Ingredient> ingredients = to(shapedRecipe.getInputs());
            if (ingredients.size() > i - empty) {
                if (width == 3) {
                    result.add(ingredients.get(i - empty));
                    continue;
                }
                if (width == 2) {
                    if (i == 0 || i == 1 || i == 3 || i == 4 || i == 6 || i == 7) {
                        result.add(ingredients.get(i - empty));
                        continue;
                    }
                }
                if (width == 1) {
                    if (i == 0 || i == 3 || i == 6) {
                        result.add(ingredients.get(i - empty));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ItemStack callTakeStack(int amount) {
        if (callGetStack().getCount() == amount)
            updateOutSlot(ItemStackUtil.empty());

        return super.callTakeStack(amount);
    }

    public void setStackSuper(ItemStack stack) {
        super.callSetStack(stack);
    }

    public int latestOutputCount;

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
        updateOutSlot(stack);
    }

    public void setOutStack(int index, int id, List<Ingredient> ingredients, int count) {
        try {
            if (index >= ingredients.size() || ingredients.isEmpty()) return;

            Ingredient input = ingredients.get(index);

            IntList matchingStacksIds = input.getMatchingStacksIds();

            if (id >= matchingStacksIds.size()) {
                id = 0;
            }

            if (matchingStacksIds.isEmpty()) return;
            callGetInventory().setStack(index + 1, RecipeMatcherUtil.getStackFromId(matchingStacksIds.getInt(id)));
            callGetInventory().getStack(index + 1).setCount(count);

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            canGet = false;
            callGetInventory().setStack(index + 1, ItemStackUtil.empty());
        }
        canGet = true;
    }

    public static List<Ingredient> to(Collection<net.minecraft.recipe.Ingredient> list) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (net.minecraft.recipe.Ingredient ingredient : list) {
            ingredients.add(Ingredient.of(ingredient));
        }
        return ingredients;
    }
}
