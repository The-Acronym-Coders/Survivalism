package com.teamacronymcoders.survivalism.common.recipe.vat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public class VatRecipe {

	protected final ResourceLocation id;
	protected final Ingredient input;
	protected final FluidStack output;
	protected final ItemStack outputStack;
	protected final float itemChance;
	protected final int jumps;

	public VatRecipe(ResourceLocation id, Ingredient input, FluidStack output, ItemStack outputStack, float itemChance, int jumps) {
		this.id = id;
		this.input = input;
		this.outputStack = outputStack;
		this.itemChance = MathHelper.clamp(itemChance, 0, 1);
		this.output = output;
		this.jumps = Math.max(1, jumps);
	}

	public VatRecipe(ResourceLocation id, Ingredient input, FluidStack output, ItemStack outputStack, int jumps) {
		this(id, input, output, outputStack, 1, jumps);
	}

	public VatRecipe(ResourceLocation id, Ingredient input, FluidStack output, int jumps) {
		this(id, input, output, ItemStack.EMPTY, 0, jumps);
	}

	public ResourceLocation getID() {
		return id;
	}

	public Ingredient getInput() {
		return input;
	}

	public FluidStack getOutput() {
		return output;
	}

	public ItemStack getOutputStack() {
		return outputStack;
	}

	public float getItemChance() {
		return itemChance;
	}

	public int getJumps() {
		return jumps;
	}

	public boolean matches(EntityPlayer jumper, ItemStack stack) {
		return input.apply(stack);
	}

}
