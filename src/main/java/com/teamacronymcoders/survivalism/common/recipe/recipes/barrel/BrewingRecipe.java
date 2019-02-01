package com.teamacronymcoders.survivalism.common.recipe.recipes.barrel;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Map;

public class BrewingRecipe extends RecipeBarrel {
    private FluidStack inputFluid;
    private Map<Ingredient, Integer> inputIngredientsMap;
    private FluidStack outputFluid;
    private int ticks;

    public BrewingRecipe() {
    }

    public BrewingRecipe(@Nonnull FluidStack inputFluid, @Nonnull Map<Ingredient, Integer> inputIngredientsMap, @Nonnull FluidStack outputFluid, int ticks) {
        this.inputFluid = inputFluid;
        this.inputIngredientsMap = inputIngredientsMap;
        this.outputFluid = outputFluid;
        this.ticks = ticks;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public void setInputFluid(FluidStack inputFluid) {
        this.inputFluid = inputFluid;
    }


    public Map<Ingredient, Integer> getInputIngredientsMap() {
        return inputIngredientsMap;
    }

    public void setInputIngredientsMap(Map<Ingredient, Integer> inputIngredientsMap) {
        this.inputIngredientsMap = inputIngredientsMap;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public void setOutputFluid(FluidStack outputFluid) {
        this.outputFluid = outputFluid;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
