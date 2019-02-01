package com.teamacronymcoders.survivalism.common.recipe.recipes.barrel;

import com.teamacronymcoders.survivalism.common.recipe.recipes.RecipeBarrel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class SoakingRecipe extends RecipeBarrel {
    private FluidStack inputFluid;
    private Ingredient inputIngredient;
    private int inputAmount;
    private ItemStack outputItemStack;
    private int decreaseAmount;
    private float decreaseChance;
    private int ticks;

    public SoakingRecipe() {
    }

    public SoakingRecipe(FluidStack inputFluid, Ingredient inputIngredient, ItemStack outputItemStack, int decreaseAmount, int ticks) {
        this.inputFluid = inputFluid;
        this.inputIngredient = inputIngredient;
        this.outputItemStack = outputItemStack;
        this.decreaseAmount = decreaseAmount;
        this.ticks = ticks;
    }

    public SoakingRecipe(FluidStack inputFluid, Ingredient inputIngredient, ItemStack outputItemStack, int decreaseAmount, float decreaseChance, int ticks) {
        this.inputFluid = inputFluid;
        this.inputIngredient = inputIngredient;
        this.outputItemStack = outputItemStack;
        this.decreaseAmount = decreaseAmount;
        this.decreaseChance = decreaseChance;
        this.ticks = ticks;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public void setInputFluid(FluidStack inputFluid) {
        this.inputFluid = inputFluid;
    }


    public Ingredient getInputIngredient() {
        return inputIngredient;
    }

    public void setInputIngredient(Ingredient inputIngredient) {
        this.inputIngredient = inputIngredient;
    }

    public ItemStack getOutputItemStack() {
        return outputItemStack;
    }

    public void setOutputItemStack(ItemStack outputItemStack) {
        this.outputItemStack = outputItemStack;
    }

    public int getDecreaseAmount() {
        return decreaseAmount;
    }

    public void setDecreaseAmount(int decreaseAmount) {
        this.decreaseAmount = decreaseAmount;
    }

    public float getDecreaseChance() {
        return decreaseChance;
    }

    public void setDecreaseChance(float decreaseChance) {
        this.decreaseChance = decreaseChance;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
