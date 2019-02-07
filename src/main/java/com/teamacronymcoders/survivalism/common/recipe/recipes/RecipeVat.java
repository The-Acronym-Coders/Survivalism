package com.teamacronymcoders.survivalism.common.recipe.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class RecipeVat {

    private ResourceLocation id;
    @Nonnull
    private Ingredient inputIngredient;
    private int inputAmount;
    private ItemStack outputStack;
    private float chanceOutput;
    private FluidStack outputFluid;
    private int jumps;

    public RecipeVat() {
    }

    /**
     * @param inputIngredient Can't Be Null
     * @param outputFluid     Can Be Null
     * @param jumps           Has to be higher than 1
     */
    public RecipeVat(ResourceLocation id, @Nonnull Ingredient inputIngredient, FluidStack outputFluid, int jumps) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.outputFluid = outputFluid;
        this.jumps = jumps;
    }

    /**
     * @param inputIngredient Can't Be Null
     * @param outputStack     Can Be Null
     * @param outputFluid     Can Be Null
     * @param jumps           Has to be higher than 1
     */
    public RecipeVat(ResourceLocation id, @Nonnull Ingredient inputIngredient, ItemStack outputStack, FluidStack outputFluid, int jumps) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.outputStack = outputStack;
        this.outputFluid = outputFluid;
        if (jumps <= 0) {
            this.jumps = jumps;
        } else {
            this.jumps = jumps;
        }
    }

    /**
     * @param inputIngredient Can't Be Null
     * @param outputStack     Can Be Null
     * @param chanceOutput    Can Be Null
     * @param outputFluid     Can Be Null
     * @param jumps           Has to be higher than 1
     */
    public RecipeVat(ResourceLocation id, @Nonnull Ingredient inputIngredient, ItemStack outputStack, float chanceOutput, FluidStack outputFluid, int jumps) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.outputStack = outputStack;
        this.chanceOutput = chanceOutput;
        this.outputFluid = outputFluid;
        if (jumps <= 0) {
            this.jumps = jumps;
        } else {
            this.jumps = jumps;
        }
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    @Nonnull
    public Ingredient getInputIngredient() {
        return inputIngredient;
    }

    public void setInputIngredient(@Nonnull Ingredient inputIngredient) {
        this.inputIngredient = inputIngredient;
    }

    public ItemStack getOutputStack() {
        return outputStack;
    }

    public void setOutputStack(ItemStack outputStack) {
        this.outputStack = outputStack;
    }

    public float getChanceOutput() {
        return chanceOutput;
    }

    public void setChanceOutput(float chanceOutput) {
        this.chanceOutput = chanceOutput;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public void setOutputFluid(FluidStack outputFluid) {
        this.outputFluid = outputFluid;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public int getInputAmount() {
        return inputAmount;
    }

    public void setInputAmount(int inputAmount) {
        this.inputAmount = inputAmount;
    }


}
