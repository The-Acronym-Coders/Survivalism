package com.teamacronymcoders.survivalism.common.recipe.recipes.barrel;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class BrewingRecipe extends RecipeBarrel {
    private FluidStack inputFluid;
    private List<Ingredient> inputIngredients;
    private FluidStack outputFluid;
    private int ticks;

    public BrewingRecipe() {}

    public BrewingRecipe(@Nonnull FluidStack inputFluid, @Nonnull List<Ingredient> inputIngredients, @Nonnull FluidStack outputFluid, int ticks) {
        this.inputFluid = inputFluid;
        this.inputIngredients = inputIngredients;
        this.outputFluid = outputFluid;
        this.ticks = ticks;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public void setInputFluid(FluidStack inputFluid) {
        this.inputFluid = inputFluid;
    }

    public List<Ingredient> getInputIngredients() {
        return inputIngredients;
    }

    public void setInputIngredients(List<Ingredient> inputIngredients) {
        this.inputIngredients = inputIngredients;
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
