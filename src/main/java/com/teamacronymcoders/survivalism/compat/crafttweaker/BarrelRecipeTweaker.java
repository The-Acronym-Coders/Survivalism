package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Credit goes out to CritFlaw
 * Please and kindly go fluff yourself!
 */
@ZenClass("mods.survivalism.Barrel")
@ZenRegister
public class BarrelRecipeTweaker {

    @ZenMethod
    public static void addBrewingRecipe(ILiquidStack inputFluid, IIngredient[] inputIngredients, ILiquidStack outputFluid, int ticks) {
        Survivalism.LATE_ADDITIONS.add(new addBrewingRecipe(inputFluid, inputIngredients, outputFluid, ticks));
    }

    @ZenMethod
    public static void addSoakingRecipe(ILiquidStack inputFluid, IIngredient inputIngredient, IItemStack outputItemStack, int ticks, @Optional int decreasingAmount, @Optional float decreasingChance) {
        Survivalism.LATE_ADDITIONS.add(new addSoakingRecipe(inputFluid, inputIngredient, outputItemStack, decreasingAmount, decreasingChance, ticks));
    }


    private static class addBrewingRecipe implements IAction {
        FluidStack inputFluid;
        List<String> names = new ArrayList<>();
        List<Ingredient> inputIngredients = new ArrayList<>();
        FluidStack outputFluid;
        int ticks;

        addBrewingRecipe(ILiquidStack inputFluid, IIngredient[] inputIngredients, ILiquidStack outputFluid, int ticks) {
            this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
            for (IIngredient ingredient : inputIngredients) {
                this.names.add(ingredient.toCommandString());
            }
            for (IIngredient ingredient : inputIngredients) {
                this.inputIngredients.add(CraftTweakerMC.getIngredient(ingredient));
            }
            this.outputFluid = CraftTweakerMC.getLiquidStack(outputFluid);
            this.ticks = ticks;
        }

        @Override
        public void apply() {
            BrewingRecipe recipe = new BrewingRecipe();
            if (inputFluid.amount <= 0 || inputFluid.amount >= TileBarrel.TANK_CAPACITY) {
                CraftTweakerAPI.logError("Input Fluid: " + inputFluid.getLocalizedName() + " is either null or has an amount less than or equal to 0mb!");
            } else {
                recipe.setInputFluid(inputFluid);
            }

            if (inputIngredients.size() <= 0 || inputIngredients.size() > 3) {
                CraftTweakerAPI.logError("Ingredient list for Brewing Recipe: " + inputFluid.getLocalizedName() + " can't be less than or equal to 0 and not greater than 3 ItemStacks big!");
            } else {
                recipe.setInputIngredients(inputIngredients);
            }

            if (outputFluid.amount <= 0 || outputFluid.amount >= TileBarrel.TANK_CAPACITY) {
                CraftTweakerAPI.logError("Output Fluid: " + outputFluid.getLocalizedName() + " has an amount less than or equal to 0mb!");
            } else {
                recipe.setOutputFluid(outputFluid);
            }

            if (ticks <= 0) {
                CraftTweakerAPI.logError("Processing Ticks can't be less than or equal to 0");
            } else {
                recipe.setTicks(ticks);
            }

            RecipeStorage.getBarrelRecipes().add(recipe);
        }

        @Override
        public String describe() {
            StringBuilder sb = new StringBuilder();
            sb.append("Added Brewing Recipe: ").append(" ");
            sb.append("Input Fluidstack: ").append(inputFluid.getLocalizedName()).append(":").append(inputFluid.amount).append(" ");
            sb.append("Input Ingredients: ").append(" ");
            for (String name : names) {
                sb.append("   ").append(name).append(" ");
            }
            sb.append("Output Fluid: ").append(outputFluid.getLocalizedName()).append(":").append(outputFluid.amount).append(" ");
            sb.append("Ticks: ").append(ticks).append(" ");
            return sb.toString();
        }
    }

    private static class addSoakingRecipe implements IAction {
        String name;
        FluidStack inputFluid;
        Ingredient ingredient;
        ItemStack outputItemStack;
        int decreaseAmount;
        float decreaseChance;
        int ticks;

        addSoakingRecipe(ILiquidStack inputFluid, IIngredient ingredient, IItemStack outputItemStack, int decreaseAmount, float decreaseChance, int ticks) {
            this.name = ingredient.toCommandString();
            this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
            this.ingredient = CraftTweakerMC.getIngredient(ingredient);
            this.outputItemStack = CraftTweakerMC.getItemStack(outputItemStack);
            if (decreaseAmount > 0 || decreaseChance <= TileBarrel.TANK_CAPACITY) {
                this.decreaseAmount = decreaseAmount;
            }
            if (decreaseChance != 0.0f) {
                this.decreaseChance = decreaseChance;
            }
            this.ticks = ticks;
        }

        @Override
        public void apply() {
            SoakingRecipe recipe = new SoakingRecipe();
            recipe.setInputFluid(inputFluid);
            recipe.setInputIngredient(ingredient);
            recipe.setOutputItemStack(outputItemStack);
            recipe.setDecreaseAmount(decreaseAmount);
            if (decreaseChance != 0.0f) {
                recipe.setDecreaseChance(decreaseChance);
            }
            recipe.setTicks(ticks);
            RecipeStorage.getBarrelRecipes().add(recipe);
        }

        @Override
        public String describe() {
            StringBuilder sb = new StringBuilder();
            sb.append("Added Soaking Recipe: ").append(" ");
            sb.append("Input Fluid: ").append(inputFluid.getLocalizedName()).append(":").append(inputFluid.amount).append(" ");
            sb.append("Input Ingredient: ").append(name).append(" ");
            sb.append("Output ItemStack: ").append(outputItemStack.getDisplayName()).append(" ");
            sb.append("Fluid Decrease Amount: ").append(decreaseAmount).append(" ");
            if (decreaseChance != 0.0f) {
                sb.append("Decrease Chance: ").append(decreaseChance).append(" ");
            }
            sb.append("Ticks: ").append(ticks).append(" ");
            return sb.toString();
        }
    }
}
