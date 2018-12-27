package com.teamacronymcoders.survivalism.common.recipe;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;

import java.util.ArrayList;
import java.util.List;

public class RecipeStorage {
    private static List<RecipeBarrel> barrelRecipes = new ArrayList<>();
    private static List<RecipeVat> vatRecipes = new ArrayList<>();


    public static List<RecipeBarrel> getBarrelRecipes() {
        return barrelRecipes;
    }

    public static List<RecipeVat> getVatRecipes() {
        return vatRecipes;
    }
}
