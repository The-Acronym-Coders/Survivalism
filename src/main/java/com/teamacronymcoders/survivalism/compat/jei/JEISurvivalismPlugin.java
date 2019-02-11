package com.teamacronymcoders.survivalism.compat.jei;

import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.compat.jei.brewing.BrewingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.brewing.BrewingRecipeWrapper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class JEISurvivalismPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.addRecipeCategories(
                new BrewingRecipeCategory(helpers.getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.handleRecipes(BrewingRecipe.class, BrewingRecipeWrapper::new, BrewingRecipeCategory.NAME);
        registry.addRecipes(BarrelRecipeManager.getBrewingRecipes(), BrewingRecipeCategory.NAME);
    }
}
