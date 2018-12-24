package com.teamacronymcoders.survivalism.common.recipe.recipes.barrel;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SoakingRecipe extends RecipeBarrel {
    private FluidStack inputFluid;
    private ItemStack inputItemStack;
    private ItemStack outputItemStack;
    private int decAmount;
    private int ticks;

    /**
     * Soaking Recipe
     *
     * @param inputFluid
     * @param inputItemStack
     * @param outputItemStack
     * @param decAmount
     * @param ticks
     */

    public SoakingRecipe(FluidStack inputFluid, ItemStack inputItemStack, ItemStack outputItemStack, int decAmount, int ticks) {
        this.inputFluid = inputFluid;
        this.inputItemStack = inputItemStack;
        this.outputItemStack = outputItemStack;
        this.decAmount = decAmount;
        this.ticks = ticks;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public ItemStack getInputItemStack() {
        return inputItemStack;
    }

    public ItemStack getOutputItemStack() {
        return outputItemStack;
    }

    public int getDecAmount() {
        return decAmount;
    }

    public int getTicks() {
        return ticks;
    }
}
