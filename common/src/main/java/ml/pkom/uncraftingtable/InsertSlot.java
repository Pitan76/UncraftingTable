package ml.pkom.uncraftingtable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InsertSlot extends Slot {
    public PlayerEntity player;
    public int recipeIndex = 0;
    public int itemIndex = 0;
    public List<Recipe<?>> latestOutRecipes = new ArrayList<>();
    public ItemStack latestItemStack = ItemStack.EMPTY;

    // OutSlotでGetできるかどうか。(バグ対策)
    public boolean canGet = true;

    public InsertSlot(Inventory inventory, int index, int x, int y, PlayerEntity player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    public void addRecipeIndex() {
        if (latestOutRecipes.isEmpty()) return;
        if (latestItemStack.isEmpty()) return;
        recipeIndex++;
        int maxIndex = latestOutRecipes.size() - 1;
        if (recipeIndex > maxIndex) {
            recipeIndex = 0;
        }
        //updateRecipe(latestOutRecipes);
        latestItemStack.setCount(getStack().getCount());
        setStack(latestItemStack);
    }

    public void removeRecipeIndex() {
        if (latestOutRecipes.isEmpty()) return;
        if (latestItemStack.isEmpty()) return;
        recipeIndex--;
        int maxIndex = latestOutRecipes.size() - 1;
        if (recipeIndex < 0) {
            recipeIndex = maxIndex;
        }
        //updateRecipe(latestOutRecipes);
        latestItemStack.setCount(getStack().getCount());
        setStack(latestItemStack);
    }

    public void updateRecipe(List<Recipe<?>> outRecipes) {
        CraftingRecipe recipe = (CraftingRecipe) outRecipes.get(recipeIndex);
        latestOutputCount = recipe.getOutput().getCount();
        if (recipe.getIngredients().size() > 5) {
            setOutStack(0, itemIndex, recipe, 1);
            setOutStack(1, itemIndex, recipe, 1);
            setOutStack(2, itemIndex, recipe, 1);
            setOutStack(3, itemIndex, recipe, 1);
            setOutStack(4, itemIndex, recipe, 1);
            setOutStack(5, itemIndex, recipe, 1);
            setOutStack(6, itemIndex, recipe, 1);
            setOutStack(7, itemIndex, recipe, 1);
            setOutStack(8, itemIndex, recipe, 1);
        } else {
            set4x4OutStack(0, itemIndex, recipe, 1);
            set4x4OutStack(1, itemIndex, recipe, 1);
            set4x4OutStack(2, itemIndex, recipe, 1);
            set4x4OutStack(3, itemIndex, recipe, 1);
        }
    }

    @Override
    public ItemStack takeStack(int amount) {
        return super.takeStack(amount);
    }

    public void setStackSuper(ItemStack stack) {
        super.setStack(stack);
    }

    public int latestOutputCount;

    @Override
    public void setStack(ItemStack stack) {
        //UncraftingTable.log(Level.INFO, "" + recipeIndex);
        //System.out.println(stack.getItem().getName().getString());
        //System.out.println(latestItemStack.getItem().getName().getString());
        super.setStack(stack);
        if (player.getWorld().isClient()) return;
        for (int i = 1; i < 10; ++i)
            ((OutSlot)player.currentScreenHandler.getSlot(i)).superSetStack(ItemStack.EMPTY);
        if (stack.isEmpty()) return;
        if (player.world == null) return;
        if (!latestItemStack.getItem().equals(stack.getItem()) && !latestItemStack.isEmpty()) {
            recipeIndex = 0;
        }
        World world = player.world;
        Collection<Recipe<?>> recipes = world.getRecipeManager().values();
        List<Recipe<?>> outRecipes = new ArrayList<>();
        for (Recipe<?> recipe : recipes) {
            if (!recipe.getType().equals(RecipeType.CRAFTING)) continue;
            if (recipe.getOutput().getCount() > stack.getCount()) continue;
            if (recipe.getOutput().getItem().equals(stack.getItem())) {
                outRecipes.add(recipe);
            }
        }
        latestOutRecipes = outRecipes;
        if (outRecipes.isEmpty()) return;
        CraftingRecipe recipe = (CraftingRecipe) outRecipes.get(recipeIndex);
        latestOutputCount = recipe.getOutput().getCount();
        if (!stack.isEmpty())
            latestItemStack = stack.copy();
        //int count = (int) Math.floor(stack.getCount() / recipe.getOutput().getCount());
        if (recipe.getIngredients().size() > 5) {
            setOutStack(0, itemIndex, recipe, 1);
            setOutStack(1, itemIndex, recipe, 1);
            setOutStack(2, itemIndex, recipe, 1);
            setOutStack(3, itemIndex, recipe, 1);
            setOutStack(4, itemIndex, recipe, 1);
            setOutStack(5, itemIndex, recipe, 1);
            setOutStack(6, itemIndex, recipe, 1);
            setOutStack(7, itemIndex, recipe, 1);
            setOutStack(8, itemIndex, recipe, 1);
        } else {
            set4x4OutStack(0, itemIndex, recipe, 1);
            set4x4OutStack(1, itemIndex, recipe, 1);
            set4x4OutStack(2, itemIndex, recipe, 1);
            set4x4OutStack(3, itemIndex, recipe, 1);
        }
    }

    public void setOutStack(int index, int id, Recipe recipe, int count) {
        try {
            if (index >= recipe.getIngredients().size() || recipe.getIngredients().size() == 0) return;
            Ingredient input = ((Ingredient) recipe.getIngredients().get(index));
            if (input.getMatchingItemIds().size() == 0 || id >= input.getMatchingItemIds().size()) return;
            inventory.setStack(index + 1, RecipeMatcher.getStackFromId(input.getMatchingItemIds().getInt(id)));
            inventory.getStack(index + 1).setCount(count);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            canGet = false;
            inventory.setStack(index + 1, ItemStack.EMPTY);
        }
        canGet = true;
    }

    public void set4x4OutStack(int index, int id, Recipe recipe, int count) {
        try {
            if (index >= recipe.getIngredients().size() || recipe.getIngredients().size() == 0) return;
            Ingredient input = ((Ingredient) recipe.getIngredients().get(index));
            if (input.getMatchingItemIds().size() == 0 || id >= input.getMatchingItemIds().size()) return;
            if (index <= 1) {
                inventory.setStack(index + 1, RecipeMatcher.getStackFromId(input.getMatchingItemIds().getInt(id)));
                inventory.getStack(index + 1).setCount(count);
            } else {
                inventory.setStack(index + 2, RecipeMatcher.getStackFromId(input.getMatchingItemIds().getInt(id)));
                inventory.getStack(index + 2).setCount(count);
            }

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            canGet = false;
            inventory.setStack(index + 1, ItemStack.EMPTY);
        }
        canGet = true;
    }

}
