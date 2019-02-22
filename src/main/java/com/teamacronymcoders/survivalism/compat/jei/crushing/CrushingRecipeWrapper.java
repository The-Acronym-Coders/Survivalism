package com.teamacronymcoders.survivalism.compat.jei.crushing;

import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipe;
import com.teamacronymcoders.survivalism.utils.helpers.HelperMath;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrushingRecipeWrapper implements IRecipeWrapper {
    public final VatRecipe recipe;

    public CrushingRecipeWrapper(VatRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(recipe.getInput().getMatchingStacks()));
        ingredients.setInputs(VanillaTypes.ITEM, stacks);
        if (!recipe.getOutputStack().isEmpty()) {
            ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
        }
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutputStack());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String input;
        int xPos;

        CrushingRecipeCategory.recipeWidth = recipeWidth;

        // Render Output Marker
        input = "->";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos - 18, 32, 4210752, false);

        input = "+";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos + 18, 32, 4210752, false);

        // Render Chance Output
        if (!recipe.getOutputStack().isEmpty()) {
            if (recipe.getItemChance() <= 0.0f | recipe.getItemChance() >= 1.0f) {
                input = "100%";
                xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
                minecraft.fontRenderer.drawString(input, xPos + 40, 17, 4210752, false);
            } else {
                input = HelperMath.round((recipe.getItemChance() * 100), 2) + "%";
                xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
                minecraft.fontRenderer.drawString(input, xPos + 40, 18, 4210752, false);
            }
        }

        // Render Jumps
        input = I18n.format("survivalism.jei.jumps") + " " + recipe.getJumps();
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos, 0, 4210752, false);
    }
}
