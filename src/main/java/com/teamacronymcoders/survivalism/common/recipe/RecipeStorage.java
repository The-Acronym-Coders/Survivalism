package com.teamacronymcoders.survivalism.common.recipe;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeStorage {
    /**
     * Recipe Lists
     */
    private static List<RecipeBarrel> barrelRecipes = new ArrayList<>();
    private static List<RecipeVat> vatRecipes = new ArrayList<>();

    /**
     * Recipe Maps
     */
    private static Map<Item, Double> bootMultiplierMap = new HashMap<>();

    static {
        bootMultiplierMap.put(Items.LEATHER_BOOTS, 1.5);
        bootMultiplierMap.put(Items.GOLDEN_BOOTS, 2.0);
        bootMultiplierMap.put(Items.IRON_BOOTS, 2.5);
        bootMultiplierMap.put(Items.DIAMOND_BOOTS, 3.0);
    }

    public static List<RecipeBarrel> getBarrelRecipes() {
        return barrelRecipes;
    }

    public static List<RecipeVat> getVatRecipes() {
        return vatRecipes;
    }

    public static Map<Item, Double> getBootMultiplierMap() {
        return bootMultiplierMap;
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

    public static RecipeVat getCrushingRecipeByID(ResourceLocation id) {
        for (RecipeVat recipe : getVatRecipes()) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }
}
