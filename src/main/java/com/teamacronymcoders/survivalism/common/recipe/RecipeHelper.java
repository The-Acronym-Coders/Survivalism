package com.teamacronymcoders.survivalism.common.recipe;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Logger;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class RecipeHelper {

    private static Logger logger = Survivalism.INSTANCE.logger;

    /**
     * Barrel Recipes
     */

    public static void addBrewingRecipe(ResourceLocation id, @Nonnull FluidStack inputFluid, @Nonnull Map<Ingredient, Integer> inputIngredientsMap, @Nonnull FluidStack outputFluid, int ticks) {
        if (inputFluid.amount <= 0) {
            logger.error("Input Fluid: " + inputFluid.getLocalizedName() + " is either null or has an amount less than or equal to 0mb!");
        } else if (inputIngredientsMap.size() <= 0 || inputIngredientsMap.size() > 3) {
            logger.error("Ingredient list for Brewing Recipe: " + inputFluid.getLocalizedName() + " can't be less than or equal to 0 and not greater than 3 ItemStacks big!");
        } else if (outputFluid.amount <= 0) {
            logger.error("Output Fluid: " + outputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
        } else if (ticks <= 0) {
            logger.error("Processing Ticks can't be less than or equal to 0");
        } else {
            RecipeBarrel brewingRecipe = new BrewingRecipe(id, inputFluid, inputIngredientsMap, outputFluid, ticks);
            RecipeStorage.getBarrelRecipes().add(brewingRecipe);
        }
    }

    public static void addSoakingRecipe(ResourceLocation id, @Nonnull FluidStack inputFluid, @Nonnull Ingredient inputIngredient, @Nonnull ItemStack outputItemStack, int decreaseAmount, int ticks) {
        if (inputFluid.amount <= 0) {
            logger.error("Input Fluid: " + inputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
        } else if (outputItemStack.isEmpty()) {
            logger.error("Output ItemStack can not be Empty");
        } else if (decreaseAmount < 0 || decreaseAmount > TileBarrel.TANK_CAPACITY) {
            logger.error("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.TANK_CAPACITY);
        } else if (ticks <= 0) {
            logger.error("Processing Ticks can't be less than or equal to 0");
        } else {
            RecipeBarrel soakingRecipe = new SoakingRecipe(id, inputFluid, inputIngredient, outputItemStack, decreaseAmount, ticks);
            RecipeStorage.getBarrelRecipes().add(soakingRecipe);
        }
    }

    public static void addSoakingRecipe(ResourceLocation id, @Nonnull FluidStack inputFluid, @Nonnull Ingredient inputIngredient, @Nonnull ItemStack outputItemStack, int decreaseAmount, float decreaseChance, int ticks) {
        if (inputFluid.amount <= 0) {
            logger.error("Input Fluid: " + inputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
        } else if (outputItemStack.isEmpty()) {
            logger.error("Output ItemStack can not be Empty");
        } else if (decreaseAmount < 0 || decreaseAmount > TileBarrel.TANK_CAPACITY) {
            logger.error("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.TANK_CAPACITY);
        } else if (decreaseChance == 0.0f || decreaseAmount > 1.0f) {
            logger.error("Decrease Chance can't be 0.0f or higher than 1.0f");
        } else if (ticks <= 0) {
            logger.error("Processing Ticks can't be less than or equal to 0");
        } else {
            RecipeBarrel soakingRecipe = new SoakingRecipe(id, inputFluid, inputIngredient, outputItemStack, decreaseAmount, decreaseChance, ticks);
            RecipeStorage.getBarrelRecipes().add(soakingRecipe);
        }
    }

}
