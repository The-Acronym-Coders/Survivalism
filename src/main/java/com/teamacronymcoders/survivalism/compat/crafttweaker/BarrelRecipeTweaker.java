package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.BrewingRecipe;
import com.teamacronymcoders.survivalism.common.recipe.recipes.barrel.SoakingRecipe;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.SurvivalismConfigs;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Ingredient, Integer> inputIngredientsMap = new HashMap<>(3);
        FluidStack outputFluid;
        int ticks;

        addBrewingRecipe(ILiquidStack inputFluid, IIngredient[] inputIngredients, ILiquidStack outputFluid, int ticks) {
            this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
            for (IIngredient ingredient : inputIngredients) {
                this.names.add(ingredient.toCommandString());
            }
            for (IIngredient ingredient : inputIngredients) {
                if (ingredient instanceof IItemStack) {
                    ItemStack stack = CraftTweakerMC.getItemStack((IItemStack) ingredient);
                    if (stack.hasTagCompound()) {
                        IngredientNBT nbt = (IngredientNBT) IngredientNBT.fromStacks(CraftTweakerMC.getItemStack(ingredient));
                        this.inputIngredientsMap.put(nbt, ingredient.getAmount());
                    }
                } else {
                    this.inputIngredientsMap.put(CraftTweakerMC.getIngredient(ingredient), ingredient.getAmount());
                }
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

            if (inputIngredientsMap.size() <= 0 || inputIngredientsMap.size() > 3) {
                CraftTweakerAPI.logError("Ingredient list for Brewing Recipe: " + inputFluid.getLocalizedName() + " can't be less than or equal to 0 and not greater than 3 ItemStacks big!");
            } else {
                recipe.setInputIngredientsMap(inputIngredientsMap);
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
            if (SurvivalismConfigs.crtVerboseLogging) {
                sb.append("Added Brewing Recipe: ").append("\n");
                sb.append("Input Fluidstack: ").append(inputFluid.getLocalizedName()).append(":").append(inputFluid.amount).append("\n");
                sb.append("Input Ingredients: ").append("\n");
                for (String name : names) {
                    sb.append(name).append("\n");
                }
                sb.append("Output Fluid: ").append(outputFluid.getLocalizedName()).append(":").append(outputFluid.amount).append("\n");
                sb.append("Ticks: ").append(ticks).append("\n");
            } else {
                sb.append("Added Brewing Recipe: ").append(" ");
                sb.append(inputFluid.getLocalizedName()).append(" ");
                for (String name : names) {
                    sb.append(name).append(" : ");
                }
                sb.append(outputFluid.getLocalizedName()).append(" ");

            }

            return sb.toString();
        }
    }

    private static class addSoakingRecipe implements IAction {
        String name1;
        String name2;
        FluidStack inputFluid;
        Ingredient ingredient;
        ItemStack outputItemStack;
        int decreaseAmount;
        float decreaseChance;
        int ticks;

        addSoakingRecipe(ILiquidStack inputFluid, IIngredient ingredient, IItemStack outputItemStack, int decreaseAmount, float decreaseChance, int ticks) {
            this.name1 = ingredient.toCommandString();
            this.name2 = outputItemStack.toCommandString();
            this.inputFluid = CraftTweakerMC.getLiquidStack(inputFluid);
            if (ingredient instanceof IItemStack) {
                ItemStack stack = CraftTweakerMC.getItemStack((IItemStack) ingredient);
                if (stack.hasTagCompound()) {
                    this.ingredient = Ingredient.fromStacks(stack);

                }
            } else {
                this.ingredient = CraftTweakerMC.getIngredient(ingredient);
            }
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
            if (inputFluid.amount <= 0 || inputFluid.amount >= TileBarrel.TANK_CAPACITY) {
                CraftTweakerAPI.logError("Input Fluid: " + inputFluid.getLocalizedName() + " is either null or has an amount less than or equal to 0mb!");
            } else {
                recipe.setInputFluid(inputFluid);
            }

            if (ingredient == null) {
                CraftTweakerAPI.logError("Ingredient for Soaking Recipe: " + name1 + " can't be Null!");
            } else {
                recipe.setInputIngredient(ingredient);
            }

            if (outputItemStack == null || outputItemStack.isEmpty()) {
                CraftTweakerAPI.logError("Output ItemStack: can't be Null!");
            } else {
                recipe.setOutputItemStack(outputItemStack);
            }

            if (decreaseAmount < 0 || decreaseAmount > TileBarrel.TANK_CAPACITY) {
                CraftTweakerAPI.logError("Decrease Amount can't be less than 0 or higher than " + TileBarrel.TANK_CAPACITY);
            } else {
                recipe.setDecreaseAmount(decreaseAmount);
            }

            if (decreaseChance < 0.0f || decreaseChance > 1.0f) {
                CraftTweakerAPI.logError("Decrease Chance can't be less than 0.0f or higher than 1.0f, if you use 1.0f skip this param and it's always a 1.0f chance!");
            } else if (decreaseChance != 0.0f) {
                recipe.setDecreaseChance(decreaseChance);
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
            if (SurvivalismConfigs.crtVerboseLogging) {
                sb.append("Added Soaking Recipe: ").append("\n");
                sb.append("Input Fluid: ").append(inputFluid.getLocalizedName()).append(":").append(inputFluid.amount).append("\n");
                sb.append("Input Ingredient: ").append(name1).append("\n");
                sb.append("Output ItemStack: ").append(name2).append("\n");
                sb.append("Fluid Decrease Amount: ").append(decreaseAmount).append("\n");
                if (decreaseChance != 0.0f) {
                    sb.append("Decrease Chance: ").append(decreaseChance).append("\n");
                }
                sb.append("Ticks: ").append(ticks).append("\n");
            } else {
                sb.append("Added Soaking Recipe for: ").append(" ");
                sb.append(inputFluid.getLocalizedName()).append(" : ").append(name1).append(" : ").append(name2);
            }
            return sb.toString();
        }
    }
}
