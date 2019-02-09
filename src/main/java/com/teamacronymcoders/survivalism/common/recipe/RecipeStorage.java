package com.teamacronymcoders.survivalism.common.recipe;

import java.util.ArrayList;
import java.util.List;

import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;

import net.minecraft.util.ResourceLocation;

public class RecipeStorage {

    /**
     * Recipe Lists
     */
    private static List<RecipeBarrel> barrelRecipes = new ArrayList<>();

    public static List<RecipeBarrel> getBarrelRecipes() {
        return barrelRecipes;
    }

    public static RecipeBarrel getBrewingRecipeByID(ResourceLocation id) {
        for (RecipeBarrel recipe : getBarrelRecipes()) {
            if (recipe instanceof BrewingRecipe) {
                if (((BrewingRecipe) recipe).getId().equals(id)) {
                    return (BrewingRecipe) recipe;
                }
            }
        }
        return null;
    }

    public static RecipeBarrel getSoakingRecipeByID(ResourceLocation id) {
        for (RecipeBarrel recipe : getBarrelRecipes()) {
            if (recipe instanceof SoakingRecipe) {
                if (((SoakingRecipe) recipe).getId().equals(id)) {
                    return (SoakingRecipe) recipe;
                }
            }
        }
        return null;
    }

}
