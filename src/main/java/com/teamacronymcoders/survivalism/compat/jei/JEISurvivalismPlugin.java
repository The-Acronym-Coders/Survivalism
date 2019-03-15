package com.teamacronymcoders.survivalism.compat.jei;

import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BarrelRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRackRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.drying.DryingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.crushing.CrushingRecipeManager;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.vat.mixing.MixingRecipeManager;
import com.teamacronymcoders.survivalism.compat.jei.barrels.brewing.BrewingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.barrels.brewing.BrewingRecipeWrapper;
import com.teamacronymcoders.survivalism.compat.jei.vats.crushing.CrushingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.vats.crushing.CrushingRecipeWrapper;
import com.teamacronymcoders.survivalism.compat.jei.vats.crushing.multiplier.MultiplierValueCategory;
import com.teamacronymcoders.survivalism.compat.jei.vats.crushing.multiplier.MultiplierValueWrapper;
import com.teamacronymcoders.survivalism.compat.jei.barrels.soaking.SoakingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.barrels.soaking.SoakingRecipeWrapper;
import com.teamacronymcoders.survivalism.compat.jei.vats.mixing.MixingRecipeCategory;
import com.teamacronymcoders.survivalism.compat.jei.vats.mixing.MixingRecipeWrapper;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEISurvivalismPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.addRecipeCategories(
                new BrewingRecipeCategory(helpers.getGuiHelper()),
                new SoakingRecipeCategory(helpers.getGuiHelper()),
                new CrushingRecipeCategory(helpers.getGuiHelper()),
                new MultiplierValueCategory(helpers.getGuiHelper()),
                new MixingRecipeCategory(helpers.getGuiHelper()),
                new DryingRackRecipeCategory(helpers.getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {
        // Brewing
        registry.addRecipes(BarrelRecipeManager.getBrewingRecipes(), BrewingRecipeCategory.NAME);
        registry.handleRecipes(BrewingRecipe.class, BrewingRecipeWrapper::new, BrewingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBarrelBrewing), BrewingRecipeCategory.NAME);

        // Soaking
        registry.addRecipes(BarrelRecipeManager.getSoakingRecipes(), SoakingRecipeCategory.NAME);
        registry.handleRecipes(SoakingRecipe.class, SoakingRecipeWrapper::new, SoakingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockBarrelSoaking), SoakingRecipeCategory.NAME);

        // Crushing
        registry.addRecipes(CrushingRecipeManager.getRecipes(), CrushingRecipeCategory.NAME);
        registry.handleRecipes(CrushingRecipe.class, CrushingRecipeWrapper::new, CrushingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockCrushingVat), CrushingRecipeCategory.NAME);

        // Multiplier Value
        registry.addRecipes(CrushingRecipeManager.getBOOTS().object2DoubleEntrySet(), MultiplierValueCategory.NAME);
        registry.handleRecipes(Object2DoubleMap.Entry.class, MultiplierValueWrapper::new, MultiplierValueCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(Items.LEATHER_BOOTS), MultiplierValueCategory.NAME);

        // Mixing
        registry.addRecipes(MixingRecipeManager.getRecipes(), MixingRecipeCategory.NAME);
        registry.handleRecipes(MixingRecipe.class, MixingRecipeWrapper::new, MixingRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockMixingVat), MixingRecipeCategory.NAME);

        // Drying Rack
        registry.addRecipes(DryingRackRecipeManager.getDryingRecipes(), DryingRackRecipeCategory.NAME);
        registry.handleRecipes(DryingRecipe.class, DryingRackRecipeWrapper::new, DryingRackRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.blockDryingRack), DryingRackRecipeCategory.NAME);
    }
}
