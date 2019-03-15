package com.teamacronymcoders.survivalism.compat.jei.barrels.brewing;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
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
        for (Ingredient ingredient : recipe.getInputItems()) {
            stacks.addAll(Arrays.asList(ingredient.getMatchingStacks()));
        }
        ingredients.setInputs(VanillaTypes.ITEM, stacks);

        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String input;
        int xPos;

        // Render Output Marker
        input = "+";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos - 18, 32, 4210752, false);

        input = "=";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos + 18, 32, 4210752, false);


        // Render Time
        if (SurvivalismConfigs.timeOrTicks) {
            input = HelperString.getDurationString(recipe.getTicks() / 20);
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
            minecraft.fontRenderer.drawString(input, xPos, 70, 4210752, false);
        } else {
            input = I18n.format("survivalism.jei.ticks") + " " + recipe.getTicks();
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
            minecraft.fontRenderer.drawString(input, xPos, 70, 4210752, false);
        }
    }
}
