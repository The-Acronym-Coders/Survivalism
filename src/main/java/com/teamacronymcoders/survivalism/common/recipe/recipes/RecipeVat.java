package com.teamacronymcoders.survivalism.common.recipe.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class RecipeVat {

    @Nonnull
    private ItemStack inputStack;

    private ItemStack outputStack;
    private FluidStack outputFluid;
    private int jumps;

    /**
     * Crushing Vat Recipe
     *
     * @param inputStack
     * @param outputStack // Can Be Null
     * @param outputFluid // Can Be Null
     * @param jumps       // Can't Be Lower than 1, Defaults to 1 if less than 1
     */
    public RecipeVat(ItemStack inputStack, ItemStack outputStack, FluidStack outputFluid, int jumps) {
        this.inputStack = inputStack;
        this.outputStack = outputStack;
        this.outputFluid = outputFluid;
        if (jumps <= 0) {
            this.jumps = jumps;
        } else {
            this.jumps = jumps;
        }
    }

    @Nonnull
    public ItemStack getInputStack() {
        return inputStack;
    }

    public ItemStack getOutputStack() {
        return outputStack;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public int getJumps() {
        return jumps;
    }
}
