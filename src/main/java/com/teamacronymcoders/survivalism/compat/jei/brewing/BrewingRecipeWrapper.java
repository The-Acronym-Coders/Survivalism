package com.teamacronymcoders.survivalism.compat.jei.brewing;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrewingRecipeWrapper implements IRecipeWrapper {

    public final BrewingRecipe recipe;

    public BrewingRecipeWrapper(BrewingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, recipe.getInput());

        List<ItemStack> stacks = new ArrayList<>();
        for (Ingredient ingredient : recipe.getInputItems().keySet()) {
            stacks.addAll(Arrays.asList(ingredient.getMatchingStacks()));
        }
        ingredients.setInputs(VanillaTypes.ITEM, stacks);

        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(HelperString.getDurationString(recipe.getTicks() / 20), 39, 70, 4210752, false);
        minecraft.fontRenderer.drawString("hh:mm:ss", 39, 80, 4210752, false);
    }
}
