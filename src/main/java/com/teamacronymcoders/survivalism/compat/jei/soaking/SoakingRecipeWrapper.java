package com.teamacronymcoders.survivalism.compat.jei.soaking;

import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoakingRecipeWrapper implements IRecipeWrapper {

    public final SoakingRecipe recipe;

    public SoakingRecipeWrapper(SoakingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(recipe.getInputItem().getMatchingStacks()));
        ingredients.setInputs(VanillaTypes.ITEM, stacks);
        ingredients.setInput(VanillaTypes.FLUID, recipe.getInput());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        // Render Chance Output
        if (recipe.getFluidUseChance() <= 0.0f | recipe.getFluidUseChance() >= 1.0f) {
            minecraft.fontRenderer.drawString("--- Use Chance ---", 12, 0, 4210752, false);
            minecraft.fontRenderer.drawString("100%", 52, 10, 4210752, false);
        } else {
            minecraft.fontRenderer.drawString("--- Use Chance ---", 10, 0, 4210752, false);
            minecraft.fontRenderer.drawString(HelperMath.round((recipe.getFluidUseChance() * 100), 2) + "%", 52, 10, 4210752, false);
        }

        // Render Ticks in HH:MM:SS
        minecraft.fontRenderer.drawString(HelperString.getDurationString(recipe.getTicks() / 20), 42, 73, 4210752, false);
    }
}
