package com.teamacronymcoders.survivalism.common.recipe.recipes.barrel;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class BrewingRecipe extends RecipeBarrel {
    private FluidStack inputFluid;
    private List<ItemStack> inputItemStacks;
    private FluidStack outputFluid;
    private int ticks;

    /**
     * Brewing Recipe
     *
     * @param inputFluid
     * @param inputItemStacks
     * @param outputFluid
     * @param ticks
     */

    public BrewingRecipe(FluidStack inputFluid, List<ItemStack> inputItemStacks, FluidStack outputFluid, int ticks) {
        this.inputFluid = inputFluid;
        this.inputItemStacks = inputItemStacks;
        this.outputFluid = outputFluid;
        this.ticks = ticks;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public List<ItemStack> getInputItemStacks() {
        return inputItemStacks;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public int getTicks() {
        return ticks;
    }
}
