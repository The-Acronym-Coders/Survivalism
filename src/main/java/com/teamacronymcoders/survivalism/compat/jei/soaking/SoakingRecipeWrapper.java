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
        String input;
        int xPos;

        // Render Chance Output
        input = "--- Use Chance ---";
        xPos = ((recipeWidth/2)-(minecraft.fontRenderer.getStringWidth(input)/2));
        minecraft.fontRenderer.drawString(input, xPos, 0, 4210752, false);

        if (recipe.getFluidUseChance() <= 0.0f | recipe.getFluidUseChance() >= 1.0f){
            input = "100%";
            xPos = (recipeWidth-minecraft.fontRenderer.getStringWidth(input))/2;
        } else {
            input = HelperMath.round((recipe.getFluidUseChance() * 100), 2) + "%";
            xPos = (recipeWidth-minecraft.fontRenderer.getStringWidth(input))/2;
        }
        minecraft.fontRenderer.drawString(input, xPos, 10, 4210752, false);

        // Render Ticks in HH:MM:SS
        input = HelperString.getDurationString(recipe.getTicks() / 20);
        xPos = (recipeWidth-minecraft.fontRenderer.getStringWidth(input))/2;
        minecraft.fontRenderer.drawString(input, xPos, 73, 4210752, false);
    }
}
