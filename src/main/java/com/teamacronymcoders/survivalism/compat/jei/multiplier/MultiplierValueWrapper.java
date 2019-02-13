package com.teamacronymcoders.survivalism.compat.jei.multiplier;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiplierValueWrapper implements IRecipeWrapper {

    public final Object2DoubleMap.Entry<Ingredient> value;

    public MultiplierValueWrapper(Object2DoubleMap.Entry<Ingredient> value) {
        this.value = value;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(value.getKey().getMatchingStacks()));
        ingredients.setInputs(VanillaTypes.ITEM, stacks);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        double value2 = value.getDoubleValue();
    }
}
