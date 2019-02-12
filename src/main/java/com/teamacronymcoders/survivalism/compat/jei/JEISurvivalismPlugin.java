package com.teamacronymcoders.survivalism.compat.jei;

import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.VatRecipeManager;
import com.teamacronymcoders.survivalism.compat.jei.brewing.BrewingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.brewing.BrewingRecipeWrapper;
import com.teamacronymcoders.survivalism.compat.jei.crushing.CrushingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.crushing.CrushingRecipeWrapper;
import com.teamacronymcoders.survivalism.compat.jei.soaking.SoakingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.soaking.SoakingRecipeWrapper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEISurvivalismPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.addRecipeCategories(
                new BrewingRecipeCategory(helpers.getGuiHelper()),
                new SoakingRecipeCategory(helpers.getGuiHelper()),
                new CrushingRecipeCategory(helpers.getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {
        // Brewing
        registry.addRecipes(BarrelRecipeManager.getBrewingRecipes(), BrewingRecipeCategory.NAME);
        registry.handleRecipes(BrewingRecipe.class, BrewingRecipeWrapper::new, BrewingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBarrel), BrewingRecipeCategory.NAME);

        // Soaking
        registry.addRecipes(BarrelRecipeManager.getSoakingRecipes(), SoakingRecipeCategory.NAME);
        registry.handleRecipes(SoakingRecipe.class, SoakingRecipeWrapper::new, SoakingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBarrel), SoakingRecipeCategory.NAME);

        // Crushing
        registry.addRecipes(VatRecipeManager.getRecipes(), CrushingRecipeCategory.NAME);
        registry.handleRecipes(VatRecipe.class, CrushingRecipeWrapper::new, CrushingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCrushingVat), CrushingRecipeCategory.NAME);
    }
}
