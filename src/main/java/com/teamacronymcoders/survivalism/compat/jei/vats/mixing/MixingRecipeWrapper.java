package com.teamacronymcoders.survivalism.compat.jei.vats.mixing;

import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class MixingRecipeWrapper implements IRecipeWrapper {
    public final MixingRecipe recipe;

    public MixingRecipeWrapper(MixingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, recipe.getMain());
        if (recipe.getSecondary() != null) {
            ingredients.setInput(VanillaTypes.FLUID, recipe.getSecondary());
        }
        if (recipe.getCatalyst() != null) {
            List<ItemStack> stacks = new ArrayList<ItemStack>(Arrays.asList(recipe.getCatalyst().getMatchingStacks()));
            ingredients.setInputs(VanillaTypes.ITEM, stacks);
        }
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        // Render Clicks
        String input = I18n.format("survivalism.jei.clicks") + " " + recipe.getClicks();
        int xpos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xpos, 0, 4210752, false);
    }
}
