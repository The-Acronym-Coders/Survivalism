package com.teamacronymcoders.survivalism.compat.crafttweaker;

import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.recipe.RecipeStorage;
import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeVat;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
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

/**
 * Credit goes out to CritFlaw
 * Please and kindly go fluff yourself!
 */
@ZenClass("mods.survivalism.CrushingVat")
@ZenRegister
public class VatRecipeTweaker {

    @ZenMethod
    public static void addCrushingRecipe(IIngredient inputIngredient, ILiquidStack outputFluidStack, int jumps, @Optional IItemStack outputStack, @Optional float chanceOutput) {
        Survivalism.LATE_ADDITIONS.add(new addCrushingRecipe(inputIngredient, outputFluidStack, jumps, outputStack, chanceOutput));
    }

    @ZenMethod
    public static void addCrushingMultiplierBoots(IItemStack stack, double multiplier) {
        Survivalism.LATE_ADDITIONS.add(new addCrushingMultiplier(stack, multiplier));
    }

    private static class addCrushingRecipe implements IAction {
        String name;
        Ingredient inputIngredient;
        int inputAmount;
        FluidStack outputFluidStack;
        int jumps;
        ItemStack outputStack;
        float chanceOutput;

        addCrushingRecipe(IIngredient inputIngredient, ILiquidStack outputFluid, int jumps, IItemStack outputStack, float chanceOutput) {
            this.name = inputIngredient.toCommandString();
            this.inputIngredient = CraftTweakerMC.getIngredient(inputIngredient);
            this.inputAmount = inputIngredient.getAmount();
            this.outputFluidStack = CraftTweakerMC.getLiquidStack(outputFluid);
            this.jumps = jumps;
            if (outputStack != null && !outputStack.isEmpty()) {
                this.outputStack = CraftTweakerMC.getItemStack(outputStack);
            }
            if (chanceOutput != 0.0f) {
                this.chanceOutput = chanceOutput;
            }
        }

        @Override
        public void apply() {
            RecipeVat recipeVat = new RecipeVat();
            recipeVat.setInputIngredient(inputIngredient);
            recipeVat.setInputAmount(inputAmount);
            recipeVat.setOutputFluid(outputFluidStack);
            if (outputStack != null && !outputStack.isEmpty()) {
                recipeVat.setOutputStack(outputStack);
            }
            if (chanceOutput != 0.0f) {
                recipeVat.setChanceOutput(chanceOutput);
            }
            if (jumps != 0) {
                recipeVat.setJumps(jumps);
            }
            RecipeStorage.getVatRecipes().add(recipeVat);
        }

        @Override
        public String describe() {
            StringBuilder sb = new StringBuilder();
            sb.append("Added Crushing Recipe:").append(" ");
            sb.append("Input: ").append(name).append(" ");
            if (outputStack != null && !outputStack.isEmpty()) {
                sb.append("Output: ").append(outputStack.getDisplayName()).append(" ");
            }
            if (chanceOutput != 0.0f) {
                sb.append("Output Chance: ").append(chanceOutput).append(" ");
            }
            sb.append("Fluid Output: ").append(outputFluidStack.getLocalizedName()).append(" ");
            sb.append("Jumps: ").append(jumps).append(" ");
            return sb.toString();
        }
    }

    private static class addCrushingMultiplier implements IAction {
        ItemStack stack;
        double multiplier;

        addCrushingMultiplier(IItemStack stack, double multiplier) {
            this.stack = CraftTweakerMC.getItemStack(stack);
            this.multiplier = multiplier;
        }

        @Override
        public void apply() {
            if (stack.isEmpty()) {
                CraftTweakerAPI.logError("Boots ItemStack can't be Null");
            }
            RecipeStorage.getBootMultiplierMap().put(stack.getItem(), multiplier);
        }

        @Override
        public String describe() {
            return "Added Multiplier: " + multiplier + " To Boots: " + stack.getDisplayName();
        }
    }
}
