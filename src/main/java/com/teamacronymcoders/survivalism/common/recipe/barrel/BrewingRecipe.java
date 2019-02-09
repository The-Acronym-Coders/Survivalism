package com.teamacronymcoders.survivalism.common.recipe.barrel;

import java.util.Map;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class BrewingRecipe {

	protected final ResourceLocation id;
	protected final FluidStack input;
	protected final Map<Ingredient, Integer> inputItems;
	protected final FluidStack output;
	protected final int ticks;

	public BrewingRecipe(ResourceLocation id, FluidStack input, Map<Ingredient, Integer> inputItems, FluidStack output, int ticks) {
		this.id = id;
		this.input = input;
		this.inputItems = inputItems;
		this.output = output;
		this.ticks = ticks;
	}

	public ResourceLocation getID() {
		return id;
	}

	public FluidStack getInput() {
		return input;
	}

	public Map<Ingredient, Integer> getInputItems() {
		return inputItems;
	}

	public FluidStack getOutput() {
		return output;
	}

	public int getTicks() {
		return ticks;
	}

	public boolean matches(TileBarrel barrel) {
		// TODO Auto-generated method stub
		return false;
	}

}
