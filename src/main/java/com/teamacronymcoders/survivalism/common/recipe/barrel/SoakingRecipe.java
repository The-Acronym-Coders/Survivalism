package com.teamacronymcoders.survivalism.common.recipe.barrel;

import com.teamacronymcoders.survivalism.common.recipe.RecipeBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public class SoakingRecipe extends RecipeBarrel {

	protected final ResourceLocation id;
	protected final FluidStack input;
	protected final Ingredient inputItem;
	protected final ItemStack output;
	protected final float fluidUseChance;
	protected final int ticks;

	public SoakingRecipe(ResourceLocation id, FluidStack input, Ingredient inputItem, ItemStack output, float fluidUseChance, int ticks) {
		this.id = id;
		this.input = input;
		this.inputItem = inputItem;
		this.output = output;
		this.fluidUseChance = MathHelper.clamp(fluidUseChance, 0, 1);
		this.ticks = ticks;
	}

	public SoakingRecipe(ResourceLocation id, FluidStack input, Ingredient inputItem, ItemStack output, int ticks) {
		this(id, input, inputItem, output, 1, ticks);
	}

	public ResourceLocation getID() {
		return id;
	}

	public FluidStack getInput() {
		return input;
	}

	public Ingredient getInputItem() {
		return inputItem;
	}

	public ItemStack getOutput() {
		return output;
	}

	public float getFluidUseChance() {
		return fluidUseChance;
	}

	public int getTicks() {
		return ticks;
	}

	public boolean matches(TileBarrel barrel) {
		return barrel.getInput().getFluid().containsFluid(input) && inputItem.apply(barrel.getInv().getStackInSlot(0));
	}

}
