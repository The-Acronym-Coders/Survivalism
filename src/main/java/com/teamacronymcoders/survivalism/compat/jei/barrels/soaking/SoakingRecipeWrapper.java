package com.teamacronymcoders.survivalism.compat.jei.barrels.soaking;

import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.MathHelper;
import com.teamacronymcoders.survivalism.utils.helpers.StringHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

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

        // Render Output Marker
        input = "+";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos - 18, 32, 4210752, false);

        input = "->";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos + 18, 32, 4210752, false);

        // Render Time
        if (SurvivalismConfigs.timeOrTicks) {
            input = StringHelper.getDurationString(recipe.getTicks() / 20);
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
            minecraft.fontRenderer.drawString(input, xPos, 63, 4210752, false);
        } else {
            input = I18n.format("survivalism.jei.ticks") + " " + recipe.getTicks();
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
            minecraft.fontRenderer.drawString(input, xPos, 63, 4210752, false);
        }

        // Render Chance Output
        if (recipe.getFluidUseChance() <= 0.0f | recipe.getFluidUseChance() >= 1.0f) {
            input = I18n.format("survivalism.jei.full.chance") + "%";
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        } else {
            input = I18n.format("survivalism.jei.use.chance") + " " + MathHelper.round((recipe.getFluidUseChance() * 100), 2) + "%";
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        }
        minecraft.fontRenderer.drawString(input, xPos, 0, 4210752, false);


    }
}
