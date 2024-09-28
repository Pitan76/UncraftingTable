package net.pitan76.uncraftingtable;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.*;

import java.util.ArrayList;
import java.util.List;

public class InsertSlot extends CompatibleSlot {
    public Player player;
    public int recipeIndex = 0;

    // アイテムのコモンタグ(鉱石辞書)のインデックス (未開発)
    public int tagItemIndex = 0;

    public List<Recipe<?>> latestOutRecipes = new ArrayList<>();
    public ItemStack latestItemStack = ItemStackUtil.empty();

    // OutSlotでGetできるかどうか。(バグ対策)
    public boolean canGet = true;

    public BookSlot bookSlot;

    public InsertSlot(Inventory inventory, int index, int x, int y, PlayerEntity player) {
        super(inventory, index, x, y);
        this.player = new Player(player);
    }

    public int getMaxTagItemIndex() {
        if (latestOutRecipes.isEmpty()) return 0;
        if (latestItemStack.isEmpty()) return 0;

        int max = 0;
        for (Ingredient ingredient : latestOutRecipes.get(recipeIndex).getIngredients()) {
            if (IngredientUtil.getMatchingStacksIds(ingredient).size() - 1 > max) {
                max = IngredientUtil.getMatchingStacksIds(ingredient).size() - 1;
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

    public static boolean ingredientsContains(DefaultedList<Ingredient> ingredients, Item item) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) continue;
            for (int id : IngredientUtil.getMatchingStacksIds(ingredient)) {
                if (ItemUtil.fromIndex(id).equals(item)) return true;
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
            int damage = stack.getDamage();
            if (damage != 0 && damage != stack.getMaxDamage()) {
                return;
            }
        }
        if (player.getWorld() == null) return;
        if (!latestItemStack.getItem().equals(stack.getItem()) && !latestItemStack.isEmpty()) {
            recipeIndex = 0;
            tagItemIndex = 0;
        }
        World world = player.getWorld();
        List<Recipe<?>> recipes = RecipeUtil.getAllRecipes(world);
        List<Recipe<?>> outRecipes = new ArrayList<>();
        for (Recipe<?> recipe : recipes) {
            if (!recipe.getType().equals(RecipeType.CRAFTING)) continue;
            if (RecipeUtil.getOutput(recipe, world).getCount() > stack.getCount()) continue;
            // Tech Reborn Disable UU Matter
            if (ItemUtil.isExist(IdentifierUtil.id("techreborn:uu_matter")) && Config.config.getBooleanOrDefault("disable_uncrafting_uu_matter", false) && ingredientsContains(recipe.getIngredients(), ItemUtil.fromId(new Identifier("techreborn:uu_matter")))) continue;

            if (RecipeUtil.getOutput(recipe, world).getItem().equals(stack.getItem())) {
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
        latestOutputCount = RecipeUtil.getOutput(recipe, world).getCount();
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
    public List<Ingredient> prettyRecipe(Recipe<?> recipe) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (!(recipe instanceof ShapedRecipe)) return recipe.getIngredients();
        ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
        int width = shapedRecipe.getWidth();

        int empty = 0;
        for (int i = 0; i < 9; i++) {
            if (shapedRecipe.getIngredients().size() > i - empty) {
                if (width == 3) {
                    ingredients.add(shapedRecipe.getIngredients().get(i - empty));
                    continue;
                }
                if (width == 2) {
                    if (i == 0 || i == 1 || i == 3 || i == 4 || i == 6 || i == 7) {
                        ingredients.add(shapedRecipe.getIngredients().get(i - empty));
                        continue;
                    }
                }
                if (width == 1) {
                    if (i == 0 || i == 3 || i == 6) {
                        ingredients.add(shapedRecipe.getIngredients().get(i - empty));
                        continue;
                    }
                }
            }
            ingredients.add(Ingredient.EMPTY);
            empty++;
        }
        return ingredients;
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

            IntList matchingStacksIds = IngredientUtil.getMatchingStacksIds(input);

            if (id >= matchingStacksIds.size()) {
                id = 0;
            }

            if (matchingStacksIds.isEmpty()) return;
            callGetInventory().setStack(index + 1, RecipeMatcher.getStackFromId(matchingStacksIds.getInt(id)));
            callGetInventory().getStack(index + 1).setCount(count);

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            canGet = false;
            callGetInventory().setStack(index + 1, ItemStackUtil.empty());
        }
        canGet = true;
    }
}
