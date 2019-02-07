package com.teamacronymcoders.survivalism.common.recipe;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Map;

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

    /**
     * Crushing Vat Recipes
     */

    public static void addCrushingRecipe(ResourceLocation id, @Nonnull Ingredient inputIngredient, @Nonnull FluidStack outputFluidStack, int jumps) {
        if (outputFluidStack.amount < 0 || outputFluidStack.amount > TileBarrel.TANK_CAPACITY) {
            logger.error("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.TANK_CAPACITY);
        } else if (jumps <= 0) {
            logger.error("Jumps can not be lower than 1, Defaulting to 1 jumps per item processed!");
        } else {
            RecipeVat vatRecipe = new RecipeVat(id, inputIngredient, outputFluidStack, jumps);
            RecipeStorage.getVatRecipes().add(vatRecipe);
        }
    }

    public static void addCrushingRecipe(ResourceLocation id, @Nonnull Ingredient inputIngredient, ItemStack outputStack, @Nonnull FluidStack outputFluidStack, int jumps) {
        if (outputStack.isEmpty()) {
            logger.error("Output Stack: " + outputStack.getDisplayName() + " Can't be ItemStack.Empty!");
        } else if (outputFluidStack.amount < 0 || outputFluidStack.amount > TileBarrel.TANK_CAPACITY) {
            logger.error("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.TANK_CAPACITY);
        } else if (jumps <= 0) {
            logger.error("Jumps can not be lower than 1, Defaulting to 1 jumps per item processed!");
        } else {
            RecipeVat vatRecipe = new RecipeVat(id, inputIngredient, outputStack, outputFluidStack, jumps);
            RecipeStorage.getVatRecipes().add(vatRecipe);
        }
    }

    public static void addCrushingRecipe(ResourceLocation id, @Nonnull Ingredient inputIngredient, ItemStack outputStack, float outputChance, @Nonnull FluidStack outputFluidStack, int jumps) {
        if (outputStack.isEmpty()) {
            logger.error("Output Stack: " + outputStack.getDisplayName() + " Can't be ItemStack.Empty!");
        } else if (outputFluidStack.amount < 0 || outputFluidStack.amount > TileBarrel.TANK_CAPACITY) {
            logger.error("Decrease Amount can't be lower than 0 or higher than " + TileBarrel.TANK_CAPACITY);
        } else if (outputChance == 0.0f) {
            logger.error("Output Chance Can't Be 0.0f!");
        } else if (jumps <= 0) {
            logger.error("Jumps can not be lower than 1, Defaulting to 1 jumps per item processed!");
        } else {
            RecipeVat vatRecipe = new RecipeVat(id, inputIngredient, outputStack, outputChance, outputFluidStack, jumps);
            RecipeStorage.getVatRecipes().add(vatRecipe);
        }
    }
}
