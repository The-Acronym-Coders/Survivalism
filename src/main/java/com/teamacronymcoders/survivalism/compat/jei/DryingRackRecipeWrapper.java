package com.teamacronymcoders.survivalism.compat.jei;

import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRecipe;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class DryingRackRecipeWrapper implements IRecipeWrapper {
    public final DryingRecipe recipe;

    public DryingRackRecipeWrapper(DryingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> stacks = new ArrayList<ItemStack>(Arrays.asList(recipe.getInput().getMatchingStacks()));
        ingredients.setInputs(VanillaTypes.ITEM, stacks);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String input;
        int xPos;

        // Render Output Marker
        input = "->";
        xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
        minecraft.fontRenderer.drawString(input, xPos+1, 30, 4210752, false);

        // Render Time
        if (SurvivalismConfigs.timeOrTicks) {
            input = HelperString.getDurationString(recipe.getTicks() / 20);
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
            minecraft.fontRenderer.drawString(input, xPos, 55, 4210752, false);
        } else {
            input = I18n.format("survivalism.jei.ticks") + " " + recipe.getTicks();
            xPos = (recipeWidth - minecraft.fontRenderer.getStringWidth(input)) / 2;
            minecraft.fontRenderer.drawString(input, xPos, 55, 4210752, false);
        }
    }
}
